package main

import (
	"bufio"
	"fmt"
	"os"
	"strconv"
)

func main() {
	f, _ := os.Open("inputs/input1.txt")
	lines := make([]int64, 0)
	// asdf
	s := bufio.NewScanner(f)
	for s.Scan() {
		i, _ := strconv.ParseInt(s.Text(), 10, 64)
		lines = append(lines, i)
	}

	for i, x := range lines {
		rest := lines[i:]
		for _, y := range rest {
			restrest := lines[i+1:]
			for _, z := range restrest {
				if x+y+z == 2020 {
					fmt.Println("got it")
					fmt.Printf("%d * %d * %d = %d\n", x, y, z, x*y*z)
					return
				}
			}
		}
	}

}
