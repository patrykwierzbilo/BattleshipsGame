## Battleships game

Application for simulating playing Battleships game over network. Application communicates via network with opponent by sending each other commands and marking effects on map. Task done during Java course in college.

## Parameters to run

* `-mode [server|client]` - mode of operation
* `-port N` - port intended for communication
* `-map map-file` - path to the file containing the map with the arrangement of ships

## Map
Example:
```
~~@~~.~~~.
@..~.~.@.~
#.~#..~.~.
..##..~..~
..~.~.@@..
.#@~..~...
.~.~.~.~.@
~.##.~.#~~
.##~..~~~~
..~.~.~~~.
```
Legend:
* `#` - ship
* `.` - water
* `~` - opponent's missed shots
* `@` - opponent's shots on target
* `?` - unknown field
