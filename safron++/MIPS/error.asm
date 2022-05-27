.data
r : 	.word 0
i : 	.word 0


.text
.globl main

main : 
	li, $s0, 3
	li, $s1, 5
	add, $t1, $s0, $s1
	sw, $t1, r

L1 : 
	li, $v0, 1
	lw, $a0, r
	syscall

L2 : 
	li, $v0, 10
	syscall


