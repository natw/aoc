package main

import (
	"bufio"
	"fmt"
	"os"
	"strconv"
	"strings"
)

func main() {
	f, _ := os.Open("inputs/8.txt")
	// f := strings.NewReader(ex1)
	s := bufio.NewScanner(f)
	// part1(s)
	part2(s)
}

type instruction struct {
	Op    string
	Value int
}

var ex1 = `nop +0
acc +1
jmp +4
acc +3
jmp -3
acc -99
acc +1
jmp -4
acc +6`

func part1(s *bufio.Scanner) {
	ops := make([]instruction, 0, 0)
	for s.Scan() {
		line := s.Text()
		op := parseInstruction(line)
		ops = append(ops, op)
	}

	acc := 0
	ptr := 0
	seen := make(map[int]bool)

	for {
		if seen[ptr] {
			fmt.Println(acc)
			return
		}
		seen[ptr] = true
		i := ops[ptr]
		switch i.Op {
		case "nop":
			ptr++
		case "acc":
			acc += i.Value
			ptr++
		case "jmp":
			ptr += i.Value
		}
	}

}

var Looped = fmt.Errorf("we looped")

func part2(s *bufio.Scanner) {
	ops := make([]instruction, 0, 0)
	toChange := make(map[int]bool)
	i := 0
	for s.Scan() {
		line := s.Text()
		op := parseInstruction(line)
		if op.Op == "nop" || op.Op == "jmp" {
			toChange[i] = true
		}
		i++
		ops = append(ops, op)
	}

	for changeMe, _ := range toChange {
		acc := 0
		ptr := 0
		seen := make(map[int]bool)
		acc, err := work2(ops, acc, ptr, changeMe, seen)
		if err == nil {
			fmt.Printf("all done! acc=%d changed=%d\n", acc, changeMe)
		} else if err == Looped {
			fmt.Printf("oops, we looped. acc=%d\n", acc)
		} else {
			fmt.Println(err)
		}
	}
}

func work2(ops []instruction, acc int, ptr int, toChange int, seen map[int]bool) (int, error) {
	for {
		if ptr >= len(ops) {
			return acc, nil
		}
		if ptr < 0 {
			return acc, fmt.Errorf("negative ptr")
		}
		if seen[ptr] {
			return acc, Looped
		}
		seen[ptr] = true
		i := ops[ptr]
		switch i.Op {
		case "nop":
			if ptr == toChange {
				ptr = i.Value
			} else {
				ptr++
			}
		case "acc":
			acc += i.Value
			ptr++
		case "jmp":
			if ptr == toChange {
				ptr++
			} else {
				ptr += i.Value
			}
		}
	}

}

func parseInstruction(line string) instruction {
	parts := strings.Split(line, " ")
	op := parts[0]
	val, _ := strconv.Atoi(parts[1])
	inst := instruction{
		Op:    op,
		Value: val,
	}
	return inst
}
