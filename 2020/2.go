package main

import (
	"bufio"
	"fmt"
	"os"
	"regexp"
	"strconv"
)

func main() {
	f, _ := os.Open("inputs/input2.txt")
	s := bufio.NewScanner(f)
	re := regexp.MustCompile(`(\d+)-(\d+) ([a-z]): (\w+)`)
	validCount := 0
	var isValid bool
	var a, b bool
	for s.Scan() {
		line := s.Text()
		m := re.FindStringSubmatch(line)

		pos1, _ := strconv.ParseInt(m[1], 10, 64)
		pos2, _ := strconv.ParseInt(m[2], 10, 64)
		pos1--
		pos2--
		letter := m[3]
		password := m[4]

		isValid = false

		a = (password[pos1] == letter[0])
		b = (password[pos2] == letter[0])
		if xor(a, b) {
			isValid = true
		}

		// count := int64(strings.Count(password, letter))
		// if count >= min && count <= max {
		// 	isValid = true
		// }

		if isValid {
			validCount++
		}
	}

	fmt.Println(validCount)
}

func xor(a bool, b bool) bool {
	return (a || b) && !(a && b)
}
