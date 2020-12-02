package main

import (
	"bufio"
	"fmt"
	"os"
	"regexp"
	"strconv"
	"strings"
)

func main() {
	f, _ := os.Open("inputs/input2.txt")
	s := bufio.NewScanner(f)
	re := regexp.MustCompile(`(\d+)-(\d+) ([a-z]): (\w+)`)
	validCount := 0
	var isValid bool
	for s.Scan() {
		line := s.Text()
		m := re.FindStringSubmatch(line)
		fmt.Println(m[1:])

		min, _ := strconv.ParseInt(m[1], 10, 64)
		max, _ := strconv.ParseInt(m[2], 10, 64)
		letter := m[3]
		password := m[4]

		isValid = false

		count := int64(strings.Count(password, letter))
		if count >= min && count <= max {
			isValid = true
		}

		if isValid {
			validCount++
		}
	}

	fmt.Println(validCount)
}
