package main

import (
	"fmt"
	"strconv"
	"strings"
)

const earliest = 1015292
const inService = "19,x,x,x,x,x,x,x,x,41,x,x,x,x,x,x,x,x,x,743,x,x,x,x,x,x,x,x,x,x,x,x,13,17,x,x,x,x,x,x,x,x,x,x,x,x,x,x,29,x,643,x,x,x,x,x,37,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,x,23"

const ex11 = 939
const ex12 = "7,13,x,x,59,x,31,19"

func main() {
	// part1(ex11, splitService(ex12)) // 295
	part1(earliest, splitService(inService))
}

func splitService(line string) (ids []int) {
	parts := strings.Split(line, ",")
	for _, part := range parts {
		if part != "x" {
			id, _ := strconv.Atoi(part)
			ids = append(ids, id)
		}
	}
	return
}

func part1(earliest int, inService []int) {
	t := earliest
	for {
		for _, id := range inService {
			if t%id == 0 {
				fmt.Println((t - earliest) * id)
				return
			}
		}
		t++
	}
}
