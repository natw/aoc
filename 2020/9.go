package main

import (
	"bufio"
	"fmt"
	"os"
	"strconv"
)

func main() {
	f, _ := os.Open("inputs/9.txt")
	preambleLen := 25

	// f := strings.NewReader(ex1)
	// preambleLen := 5

	s := bufio.NewScanner(f)
	var nums []int
	for s.Scan() {
		line := s.Text()
		n, _ := strconv.Atoi(line)
		nums = append(nums, n)
	}
	firstInvalid := part1(nums, preambleLen)

	part2(nums, firstInvalid)

}

func part2(nums []int, firstInvalid int) {
	for i := range nums {
		for windowLen := 1; windowLen < len(nums)-i; windowLen++ {
			window := nums[i : windowLen+i]
			sum := SumSlice(window)
			if sum == firstInvalid {
				fmt.Printf("sum(%+v) == %d\n", window, firstInvalid)
				min := Min(window)
				max := Max(window)
				fmt.Printf("%d + %d = %d\n", min, max, min+max)
				return
			}

		}
	}
}

func Min(nums []int) int {
	min := nums[0]
	for _, n := range nums {
		if n < min {
			min = n
		}
	}
	return min
}

func Max(nums []int) int {
	max := nums[0]
	for _, n := range nums {
		if n > max {
			max = n
		}
	}
	return max
}

func SumSlice(nums []int) int {
	sum := 0
	for _, n := range nums {
		sum += n
	}
	return sum
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

func part1(nums []int, preambleLen int) int {
	for i, n := range nums[preambleLen:] {
		sums := getSums(nums[i : preambleLen+i])
		if sums[n] == false {
			fmt.Println(n)
			return n
		}
	}
	return 0
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
