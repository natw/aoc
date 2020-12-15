package main

import "fmt"

var input = []int{1, 12, 0, 20, 8, 16}

func main() {

	fmt.Println("expect 175594:")
	part2(ex0)
	fmt.Println("expect 2578:")
	part2(ex1)

	part2(input)
}

var ex0 = []int{0, 3, 6} // 2020th = 436
var ex1 = []int{1, 3, 2} // 2020th = 1
var ex2 = []int{2, 1, 3} // 2020th = 10
// 1,2,3, the 2020th number spoken is 27.
// 2,3,1, the 2020th number spoken is 78.
// 3,2,1, the 2020th number spoken is 438.
// 3,1,2, the 2020th number spoken is 1836.

func part1v2(input []int) {
	spoken := make(map[int][]int)
	var lastSaid int
	turn := 0

	for _, num := range input {
		turn++
		spoken[num] = append(spoken[num], turn)
		lastSaid = num
	}

	var say int

	for turn < 2022 {
		turn++
		if len(spoken[lastSaid]) == 1 { // last time was the first time
			say = 0
		} else {
			turns := spoken[lastSaid]
			say = turns[len(turns)-1] - turns[len(turns)-2]
		}
		spoken[say] = append(spoken[say], turn)
		lastSaid = say
		if turn == 2020 {
			fmt.Printf("[%04d] said = %d\n", turn, say)
		}
	}
}
func part2(input []int) {
	spoken := make(map[int][]int)
	var lastSaid int
	turn := 0

	for _, num := range input {
		turn++
		spoken[num] = append(spoken[num], turn)
		lastSaid = num
	}

	var say int

	for {
		turn++
		if len(spoken[lastSaid]) == 1 { // last time was the first time
			say = 0
		} else {
			turns := spoken[lastSaid]
			say = turns[len(turns)-1] - turns[len(turns)-2]
		}
		spoken[say] = append(spoken[say], turn)
		lastSaid = say
		if turn == 30000000 {
			fmt.Printf("[%04d] said = %d\n", turn, say)
			return
		}
	}
}
