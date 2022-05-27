.data
i : 	.word 0
f_q : 	.word 0
f_r : 	.word 0


.text
.globl main

main : 
	li, $s0, 1
	li, $s1, 2
	add, $t1, $s0, $s1
	li, $s0, 12
	li, $s1, 2
	mul, $t2, $s0, $s1
	li, $s1, 4
	div, $t3, $t2, $s1
	mul, $t4, $t1, $t3
	sw, $t4, i

L1 : 
	li, $v0, 10
	syscall

f : 
	sw, $a0, f_q

f_L1 : 
	li, $s0, 0
	sw, $s0, f_r

f_L2 : 
	jr, $ra


