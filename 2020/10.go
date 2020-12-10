package main

import (
	"bufio"
	"fmt"
	"io"
	"os"
	"sort"
	"strconv"
)

func main() {
	f, _ := os.Open("inputs/10.txt")
	// f := strings.NewReader(ex1)
	part1(f)
}

var ex1 = `16
10
15
5
1
11
7
19
6
12
4
`

func part1(f io.Reader) {
	nums := getNums(f)
	sort.Ints(nums)
	diffs := make(map[int]int)

	j := 0

	for _, output := range nums {
		diff := output - j
		diffs[diff]++
		j = output
	}
	diffs[3]++

	fmt.Printf("%+v\n", diffs)

	fmt.Println(diffs[1] * diffs[3])

}

func getNums(f io.Reader) []int {
	var nums []int
	s := bufio.NewScanner(f)
	for s.Scan() {
		line := s.Text()
		num, _ := strconv.Atoi(line)
		nums = append(nums, num)
	}
	return nums
}
