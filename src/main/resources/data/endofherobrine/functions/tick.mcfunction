scoreboard players set Total Destruction 0
scoreboard players operation Total Destruction += @a KilledMobs
scoreboard players operation Total Destruction += @a BlocksChanged
execute if score Total Destruction matches 1.. run advancement grant @a only endofherobrine:story/root