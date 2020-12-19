package main

import (
	"fmt"
)

func main() {
	a := []int{1, 2, 3, 4, 5}
	i := 2
	fmt.Printf("%v\n", append(a[:i-1], a[i+2:]...))
}
