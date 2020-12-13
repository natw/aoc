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
	// part1(earliest, splitService(inService))
	// part2(split(ex12)) //1068781
	part2(split(inService))
}

func split(line string) (ids []int) {
	parts := strings.Split(line, ",")
	for _, part := range parts {
		if part == "x" {
			part = "0"
		}
		id, _ := strconv.Atoi(part)
		ids = append(ids, id)
	}
	return
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

func part2(ids []int) {
	for i := 1; i >= 0; i++ {
		n := i * ids[0]
		if foundIt(n, ids) {
			fmt.Println("got it")
			fmt.Println(n)
			return
		}
	}
}

func foundIt(start int, ids []int) bool {
	for i, check := range ids {
		if check == 0 {
			continue
		}
		if (start+i)%check != 0 {
			return false
		}
	}
	return true
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
