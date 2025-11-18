import random

def initial_map(width, height):
    map_grid = [[0 for _ in range(width)] for _ in range(height//2)]

    #generating half map randomly first 
    #mirroring it over other half to ensure fairness among players
    for row in range(height//2):
        for cell in range(width):
            rand_num = random.random()

            #probabilities: tree: 30%, water: 30%, open land = 40%
            if rand_num < 0.3:
                map_grid[row][cell] = 'w'
            elif rand_num < 0.6: 
                map_grid[row][cell] = 't'
            else: 
                map_grid[row][cell] = '.'

    #copying middle row twice in case of odd height of map
    if height%2 ==1:
            map_grid.append(map_grid[-1].copy())

    for i in range(height//2 -1,-1,-1):
        map_grid.append(map_grid[i].copy())

    return map_grid

def get_neighbors(col_num, row_num, map_grid, n_cols, n_rows):
    #gets upto 8 neighbors around the tile (including diagonals)
    neighbors = []
    for i in [-1, 0, 1]:
        for j in [-1, 0, 1]:
            nx = row_num + i
            ny = col_num + j
            if i == 0 and j==0: #Skipping the cell itself
                continue
            if 0 <= nx < n_rows and 0 <= ny < n_cols: 
                neighbors.append(map_grid[nx][ny])
    return neighbors

def map_refinement(map_grid):
    n_cols = len(map_grid[0]) 
    n_rows = len(map_grid)

    new_grid = [['.' for _ in range(n_cols)] for _ in range(n_rows)]
    for row_num in range(n_rows):
        for col_num in range(n_cols):
            neighbors = get_neighbors(col_num, row_num, map_grid,n_cols, n_rows)
            n_trees = neighbors.count('t')
            n_water = neighbors.count('w')

            #Rules for map refinement
            if n_trees>3: #trees have higher priority than water
                new_grid[row_num][col_num] = 't'
            elif n_water>=4: 
                new_grid[row_num][col_num] = 'w'
            elif n_water<3:
                new_grid[row_num][col_num] = '.'     
            else:
                new_grid[row_num][col_num] = map_grid[row_num][col_num]

    return new_grid

def clear_base_area(height, map_grid):
    #Clears area at the top and bottom left corners for both bases and Goldmines
    num = min((height//4)+2,14)
    clear_row = ['.'] * num
    for i in range(0,num):
        map_grid[i][:num] = clear_row
            
    for j in range(height-num,height):
        map_grid[j][:num] = clear_row
    return map_grid

def export_to_xml(filename, map_grid, players, entities):
    height = len(map_grid)
    width = len(map_grid[0]) if height > 0 else 0

    with open(filename, "w") as f:
        f.write("<gamestate>\n")

        f.write(f'\t<entity id="0">\n')
        f.write(f'\t\t<type>map</type>\n')
        f.write(f'\t\t<width>{width}</width>\n')
        f.write(f'\t\t<height>{height}</height>\n')
        f.write(f'\t\t<background>\n')
        for row in map_grid:
            row_str = "".join(row)
            f.write(f'\t\t\t<row>{row_str}</row>\n')
        f.write(f'\t\t</background>\n')
        f.write(f'\t</entity>\n\n')

        for player in players:
            f.write(f'\t<entity id="{player["id"]}">\n')
            f.write(f'\t\t<type>WPlayer</type>\n')
            f.write(f'\t\t<gold>{player["gold"]}</gold>\n')
            f.write(f'\t\t<wood>{player["wood"]}</wood>\n')
            f.write(f'\t\t<owner>{player["owner"]}</owner>\n')
            f.write(f'\t</entity>\n\n')

        for e in entities:
            f.write(f'\t<entity id="{e["id"]}">\n')
            f.write(f'\t\t<type>{e["type"]}</type>\n')
            if "x" in e and "y" in e:
                f.write(f'\t\t<x>{e["x"]}</x>\n')
                f.write(f'\t\t<y>{e["y"]}</y>\n')
            if "owner" in e:
                f.write(f'\t\t<owner>{e["owner"]}</owner>\n')
            if "remaining_gold" in e:
                f.write(f'\t\t<remaining_gold>{e["remaining_gold"]}</remaining_gold>\n')
            if "current_hitpoints" in e:
                f.write(f'\t\t<current_hitpoints>{e["current_hitpoints"]}</current_hitpoints>\n')
            f.write(f'\t</entity>\n\n')

        f.write("</gamestate>\n")


random.seed(1)
height = 32
width = 32
#building initial random map
map_grid = initial_map(width,height)

#Refining map with rules over multiple iterations
for i in range(0,5):
    map_grid = map_refinement(map_grid)

#clearing area for bases and goldmine
map_grid = clear_base_area(height, map_grid)

#adding players and entities
players = [
    {"id": 14, "owner": "player1", "gold": 2000, "wood": 1500},
    {"id": 15, "owner": "player2", "gold": 2000, "wood": 1500},
]
entities = [
    {"id": 3, "type": "WGoldMine", "x": 0, "y": 0, "remaining_gold": 100000, "current_hitpoints": 25500},
    {"id": 4, "type": "WGoldMine", "x": 0, "y": height-1, "remaining_gold": 100000, "current_hitpoints": 25500},
    {"id": 5, "type": "WTownhall", "x": 3, "y": 1, "owner": "player1", "current_hitpoints": 2400},
    {"id": 6, "type": "WTownhall", "x": 3, "y": height-3, "owner": "player2", "current_hitpoints": 2400},
]
export_to_xml("S3-CS387\maps\map.xml", map_grid, players, entities)