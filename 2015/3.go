package main

import (
	"bufio"
	"fmt"
	"os"
)

func main() {
	part1()
}

func part1() {

	f, _ := os.Open("inputs/3.txt")
	r := bufio.NewReader(f)
	inst, _ := r.ReadString('\n')
	work(inst)

	// ex1 := ">"
	// work(ex1)

	// work("^>v<")
	// work("^v^v^v^v^v")
}

type point struct {
	x int64
	y int64
}

func work(inst string) {
	fmt.Println("working")
	var x, y int64

	visited := make(map[point]int64)

	p := point{x, y}
	visited[p] = visited[p] + 1
	fmt.Printf("visiting %+v\n", p)
	for _, step := range inst {
		switch step {
		case '>':
			x++
		case '<':
			x--
		case '^':
			y++
		case 'v':
			y--
		}
		p := point{x, y}
		fmt.Printf("visiting %+v\n", p)
		visited[p] = visited[p] + 1
	}

	var ans int

	for _, value := range visited {
		if value > 1 {
			ans++
		}
	}
	fmt.Printf("visited %d houses, %d more than once\n", len(visited), ans)
}
