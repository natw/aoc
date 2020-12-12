package main

import (
	"bufio"
	"fmt"
	"io"
	"os"
	"sort"
	"strconv"
	"strings"
)

func main() {
	// f, _ := os.Open("inputs/10.txt")
	// part1(f)

	f1 := strings.NewReader(ex1) // 8
	part2(f1)
	fmt.Println("expected 8")

	f2 := strings.NewReader(ex2) // 19208
	part2(f2)
	fmt.Println("expected 19208")

	f, _ := os.Open("inputs/10.txt")
	part2(f)
}

// [0 1 2 3 4 7 8 9 10 11 14 17 18 19 20 23 24 25 28 31 32 33 34 35 38 39 42 45 46 47 48 49 52]
var ex2 = `28
33
18
42
31
14
46
20
48
47
24
23
49
45
19
38
39
11
1
32
25
35
8
17
7
9
4
2
34
10
3
`

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

func part2(f io.Reader) {
	nums := getNums(f)
	sort.Ints(nums)

	paths := map[int]int{
		0: 1,
	}

	max := Max(nums)
	nums = append(nums, max+3)

	for _, a := range nums {
		paths[a] = paths[a-1] + paths[a-2] + paths[a-3]
	}
	fmt.Println(paths[max])
}

func isValid(nums []int) bool {
	for i := range nums {
		if i == 0 {
			continue
		}
		if nums[i]-nums[i-1] > 3 {
			return false
		}
	}
	return true
}

func without(i int, nums []int) []int {
	x := nums[i+1:]
	return append(nums[:i], x...)
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
