package main

import (
	"bufio"
	"fmt"
	"io"
	"os"
	"strconv"
	"strings"
)

var ex1 = `2 * 3 + (4 * 5)`
var ex2 = `5 + (8 * 3 + 9 + 3 * 4 * 3)`
var ex3 = `5 * 9 * (7 * 3 * 3 + 9 * 3 + (8 + 6 * 4))`       // 12240
var ex4 = `((2 + 4 * 9) * (6 + 9 * 8 + 6) + 6) + 2 + 4 * 2` // 13632

func main() {
	// f, _ := os.Open("inputs/18.txt")
	// part1(f)
	// fmt.Println("26:")
	// part1(strings.NewReader(ex1))
	// fmt.Println("437:")
	// part1(strings.NewReader(ex2))
	// fmt.Println("12240:")
	// part1(strings.NewReader(ex3))
	// fmt.Println("13632:")
	// part1(strings.NewReader(ex4))

	fmt.Println("46:")
	part2(strings.NewReader(ex1))
	fmt.Println("1445:")
	part2(strings.NewReader(ex2))
	fmt.Println("669060:")
	part2(strings.NewReader(ex3))
	fmt.Println("23340:")
	part2(strings.NewReader(ex4))

	f, _ := os.Open("inputs/18.txt")
	part2(f)
}

func part2(f io.Reader) {
	s := bufio.NewScanner(f)
	total := 0
	for s.Scan() {
		line := s.Text()
		total += calc2(line)
	}
	fmt.Println(total)
}

func part1(f io.Reader) {
	s := bufio.NewScanner(f)
	total := 0
	for s.Scan() {
		line := s.Text()
		total += calc1(line)
	}
	fmt.Println(total)
}

func add(a int, b int) int {
	return a + b
}

func multiply(a int, b int) int {
	return a * b
}
func justOps2(line string) int {
	parts := strings.Split(line, " ")

	i := 0
	for {
		if i >= len(parts) {
			break
		}
		p := parts[i]
		if p == "+" {
			a, _ := strconv.Atoi(parts[i-1])
			b, _ := strconv.Atoi(parts[i+1])
			ps := append(parts[:i-1], fmt.Sprintf("%d", a+b))
			parts = append(ps, parts[i+2:]...)

		} else {
			i++
		}
	}

	var op = add
	total := 0
	// fmt.Printf("just ops: %v\n", parts)
	for _, c := range parts {
		switch c {
		case "+":
			op = add
		case "*":
			op = multiply
		default:
			// fmt.Printf("num: %s\n", c)
			i, _ := strconv.Atoi(c)
			total = op(total, i)
			// fmt.Printf("new total: %d\n", total)
		}
	}
	return total
}
func justOps(line string) int {
	var op = add
	total := 0
	// fmt.Printf("just ops: %s\n", line)
	parts := strings.Split(line, " ")
	for _, c := range parts {
		switch c {
		case "+":
			op = add
		case "*":
			op = multiply
		default:
			// fmt.Printf("num: %s\n", c)
			i, _ := strconv.Atoi(c)
			total = op(total, i)
			// fmt.Printf("new total: %d\n", total)
		}
	}
	return total
}

func calc2(line string) int {
	for {
		start := strings.LastIndex(line, "(")
		if start == -1 {
			// fmt.Printf("breaking: %s\n", line)
			break
		}
		end := strings.Index(line[start:], ")") + start
		innerPart := line[start+1 : end]
		line = fmt.Sprintf("%s%d%s", line[:start], calc2(innerPart), line[end+1:])
	}
	return justOps2(line)
}
func calc1(line string) int {
	for {
		start := strings.LastIndex(line, "(")
		if start == -1 {
			// fmt.Printf("breaking: %s\n", line)
			break
		}
		end := strings.Index(line[start:], ")") + start
		innerPart := line[start+1 : end]
		line = fmt.Sprintf("%s%d%s", line[:start], calc1(innerPart), line[end+1:])
	}
	return justOps(line)
}
