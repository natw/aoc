package main

import (
	"bufio"
	"fmt"
	"os"
	"strconv"
)

func main() {
	f, _ := os.Open("inputs/9.txt")
	// f := strings.NewReader(ex1)
	s := bufio.NewScanner(f)
	var nums []int
	for s.Scan() {
		line := s.Text()
		n, _ := strconv.Atoi(line)
		nums = append(nums, n)
	}
	part1(nums, 25)
}

var ex1 = `35
20
15
25
47
40
62
55
65
95
102
117
150
182
127
219
299
277
309
576`

func part1(nums []int, preambleLen int) {
	for i, n := range nums[preambleLen:] {
		sums := getSums(nums[i : preambleLen+i])
		if sums[n] == false {
			fmt.Println(n)
			return
		}
	}
}

func getSums(nums []int) map[int]bool {
	sums := make(map[int]bool)
	for i, n := range nums {
		for _, n2 := range nums[i:] {
			sums[n+n2] = true
		}
	}
	return sums

}
