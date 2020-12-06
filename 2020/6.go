package main

import (
	"bufio"
	"fmt"
	"io"
	"os"
)

func main() {
	// part1()
	part2()
}

func part1() {
	f, _ := os.Open("inputs/6.txt")
	work1(f)
}
func part2() {
	f, _ := os.Open("inputs/6.txt")
	work2(f)
}

func work1(f io.Reader) {
	groups := getGroups(f)
	totalCount := 0
	for _, group := range groups {
		fmt.Println(group)
		yesses := make(map[rune]bool)
		for _, line := range group {
			for _, question := range line {
				yesses[question] = true
			}
		}
		fmt.Printf("%v - %d\n", yesses, len(yesses))
		totalCount += len(yesses)
	}
	fmt.Println(totalCount)
}

func work2(f io.Reader) {
	groups := getGroups(f)
	totalCount := 0
	for _, group := range groups {
		fmt.Println(group)
		yesses := make(map[rune]int)
		count := 0
		for _, line := range group {
			for _, question := range line {
				yesses[question]++
			}
		}
		for _, v := range yesses {
			if v == len(group) {
				count++
			}
		}
		totalCount += count
	}
	fmt.Println(totalCount)
}

func getGroups(f io.Reader) [][]string {
	s := bufio.NewScanner(f)
	var groups [][]string
	var group []string
	for s.Scan() {
		line := s.Text()
		if line == "" {
			groups = append(groups, group)
			group = []string{}
		} else {
			group = append(group, line)
		}
	}
	groups = append(groups, group)
	return groups
}
