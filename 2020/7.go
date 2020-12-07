package main

import (
	"bufio"
	"fmt"
	"io"
	"os"
	"strconv"
	"strings"
)

func main() {
	f, _ := os.Open("inputs/7.txt")
	lines := getLines(f)
	// part1(lines)
	part2(lines)
}

func getLines(f io.Reader) []string {
	s := bufio.NewScanner(f)
	var lines []string
	for s.Scan() {
		line := s.Text()
		lines = append(lines, line)
	}
	return lines
}

type containment struct {
	Color  string
	Number int
}

func makeRules(lines []string) map[string][]containment {
	rules := make(map[string][]containment)
	for _, line := range lines {
		parts := strings.Split(line, " bags contain ")
		containerColor := parts[0]
		cmDefs := strings.Split(strings.TrimSuffix(parts[1], "."), ", ")
		for _, cmDef := range cmDefs {
			ps := strings.Split(cmDef, " ")
			num, _ := strconv.Atoi(ps[0])
			cm := containment{
				Number: num,
				Color:  strings.Join(ps[1:3], " "),
			}
			rules[containerColor] = append(rules[containerColor], cm)
		}
	}
	return rules
}

func part2(lines []string) {
	rules := makeRules(lines)

	fmt.Println(countInnerBags(rules, "shiny gold"))
}

func countInnerBags(rules map[string][]containment, outerColor string) int {
	count := 0
	for _, cm := range rules[outerColor] {
		innerCount := countInnerBags(rules, cm.Color)
		count += cm.Number * (innerCount + 1)
	}
	return count
}

func part1(lines []string) {
	rules := makeRules(lines)

	// for k, v := range rules {
	// 	fmt.Printf("%s - %+v\n", k, v)
	// }

	count := 0
	for k, _ := range rules {
		if canContain(rules, k, "shiny gold") {
			count++
		}
	}
	fmt.Println(count)
}

func canContain(rules map[string][]containment, outer string, inner string) bool {
	for _, cm := range rules[outer] {
		if cm.Color == inner || canContain(rules, cm.Color, inner) {
			return true
		}
	}
	return false
}
