scoreboard players reset * DestructionDis
scoreboard players set Total DestructionDis 0
execute as @e[type=minecraft:player] run scoreboard players operation @s DestructionDis = @s Destruction
execute as @e[type=minecraft:player] run scoreboard players operation Total DestructionDis += @s Destruction
execute if score Total DestructionDis matches 1.. run advancement grant @a only endofherobrine:story/root