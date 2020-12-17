package main

import (
	"fmt"
	"strings"
	"sync"
)

var input = `##..#.#.
#####.##
#######.
#..#..#.
#.#...##
..#....#
....#..#
..##.#..`

var ex1 = `.#.
..#
###`

func main() {
	// part1(ex1)
	// part1(input)
	// part2(ex1)
	part2(input)
}

func part1(input string) {
	lines := strings.Split(input, "\n")
	initialState := make(map[coord]bool)
	z := 0
	for y, line := range lines {
		for x, cell := range line {
			if cell == '#' {
				c := coord{x, y, z}
				initialState[c] = true
			}
		}
	}

	state := initialState
	for i := 0; i < 6; i++ {
		state = tick1(state)
	}

	fmt.Println(len(state))

}

func part2(input string) {
	lines := strings.Split(input, "\n")
	initialState := make(map[coord2]bool)
	z := 0
	w := 0
	for y, line := range lines {
		for x, cell := range line {
			if cell == '#' {
				c := coord2{x, y, z, w}
				initialState[c] = true
			}
		}
	}

	state := initialState
	for i := 0; i < 6; i++ {
		state = tick2(state)
	}

	fmt.Println(len(state))

}

func tick2(state map[coord2]bool) map[coord2]bool {
	newState := make(map[coord2]bool)
	var wg sync.WaitGroup
	var workerCount = 6
	activeCoords := make(chan coord2)
	jobs := make(chan coord2)

	for i := 0; i < workerCount; i++ {
		wg.Add(1)
		go func() {
			defer wg.Done()
			for c := range jobs {
				if willBeActive2(state, c) {
					activeCoords <- c
				}
			}
		}()
	}

	go func() {
		for c := range state {
			jobs <- c
			for _, n := range neighbors2(c) {
				jobs <- n
			}
		}
		close(jobs)
	}()

	go func() {
		wg.Wait()
		close(activeCoords)
	}()

	for c := range activeCoords {
		newState[c] = true
	}
	return newState
}

func tick1(state map[coord]bool) map[coord]bool {
	newState := make(map[coord]bool)
	var wg sync.WaitGroup
	var workerCount = 4
	activeCoords := make(chan coord)
	jobs := make(chan coord)

	for i := 0; i < workerCount; i++ {
		wg.Add(1)
		go func() {
			defer wg.Done()
			for c := range jobs {
				if willBeActive(state, c) {
					activeCoords <- c
				}
			}
		}()
	}

	go func() {
		for c := range state {
			jobs <- c
			for _, n := range neighbors(c) {
				jobs <- n
			}
		}
		close(jobs)
	}()

	go func() {
		wg.Wait()
		close(activeCoords)
	}()

	for c := range activeCoords {
		newState[c] = true
	}
	return newState
}

func willBeActive2(state map[coord2]bool, c coord2) bool {
	ns := neighbors2(c)
	activeNeighbors := 0
	for _, ns := range ns {
		if state[ns] {
			activeNeighbors++
		}
	}
	if state[c] {
		if activeNeighbors == 2 || activeNeighbors == 3 {
			return true
		}
	} else {
		if activeNeighbors == 3 {
			return true
		}
	}
	return false
}

func willBeActive(state map[coord]bool, c coord) bool {
	ns := neighbors(c)
	activeNeighbors := 0
	for _, ns := range ns {
		if state[ns] {
			activeNeighbors++
		}
	}
	if state[c] {
		if activeNeighbors == 2 || activeNeighbors == 3 {
			return true
		}
	} else {
		if activeNeighbors == 3 {
			return true
		}
	}
	return false
}

type coord [3]int
type coord2 [4]int

func neighbors(p coord) []coord {
	var ns []coord
	x := p[0]
	y := p[1]
	z := p[2]
	ns = []coord{
		{x, y, z - 1},
		{x, y, z + 1},
		{x, y - 1, z - 1},
		{x, y - 1, z + 1},
		{x, y - 1, z},
		{x, y + 1, z - 1},
		{x, y + 1, z + 1},
		{x, y + 1, z},
		{x - 1, y - 1, z - 1},
		{x - 1, y - 1, z + 1},
		{x - 1, y - 1, z},
		{x - 1, y + 1, z - 1},
		{x - 1, y + 1, z + 1},
		{x - 1, y + 1, z},
		{x - 1, y, z},
		{x - 1, y, z - 1},
		{x - 1, y, z + 1},
		{x + 1, y - 1, z - 1},
		{x + 1, y - 1, z + 1},
		{x + 1, y - 1, z},
		{x + 1, y + 1, z - 1},
		{x + 1, y + 1, z + 1},
		{x + 1, y + 1, z},
		{x + 1, y, z},
		{x + 1, y, z - 1},
		{x + 1, y, z + 1},
	}
	return ns
}

func neighbors2(p coord2) []coord2 {
	var ns []coord2
	deltas := []int{-1, 0, 1}
	for _, dx := range deltas {
		for _, dy := range deltas {
			for _, dz := range deltas {
				for _, dw := range deltas {
					if dx == 0 && dy == 0 && dz == 0 && dw == 0 {
						continue
					}
					ns = append(ns, coord2{p[0] + dx, p[1] + dy, p[2] + dz, p[3] + dw})
				}
			}
		}
	}
	return ns
}
