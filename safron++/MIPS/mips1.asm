.data
str1 :	.asciiz "\n"
c : 	.word 0
i : 	.word 0
fib_x : 	.word 0
fib_y : 	.word 0
fib_n : 	.word 0


.text
.globl main

main : 
	li, $s0, 0
	sw, $s0, i

L1 : 
	li, $s0, 0
	sw, $s0, c

L2 : 
	lw, $s0, c
	li, $s1, 10
	slt, $t1, $s0, $s1
	beq, $t1, $zero, L7

L3 : 
	lw, $a0, c
	jal, fib
	sw, $v1, i

L4 : 
	li, $v0, 1
	lw, $a0, i
	syscall

L5 : 
	li, $v0, 4
	la, $a0, str1
	syscall

L6 : 
	lw, $s0, c
	li, $s1, 1
	add, $t1, $s0, $s1
	sw, $t1, c
	jal, L2

L7 : 
	li, $v0, 10
	syscall

fib : 
	sw, $a0, fib_n

fib_L1 : 
	lw, $s0, fib_n
	li, $s1, 1
	sle, $t1, $s0, $s1
	beq, $t1, $zero, fib_L2
	lw, $s0, fib_n
	move, $v1, $s0
	jr, $ra

fib_L2 : 
	lw, $s0, fib_n
	li, $s1, 1
	sgt, $t1, $s0, $s1
	beq, $t1, $zero, fib_L8

fib_L3 : 
	lw, $s0, fib_n
	li, $s1, 1
	sub, $t1, $s0, $s1
	sw, $t1, fib_x

fib_L4 : 
	lw, $a0, fib_x
	addi, $sp, $sp, -16
	sw, $ra, 12($sp)
	lw, $s0, fib_x
	sw, $s0, 8($sp)
	lw, $s0, fib_y
	sw, $s0, 4($sp)
	lw, $s0, fib_n
	sw, $s0, 0($sp)
	jal, fib
	lw, $ra, 12($sp)
	lw, $s0, 8($sp)
	sw, $s0, fib_x
	lw, $s0, 4($sp)
	sw, $s0, fib_y
	lw, $s0, 0($sp)
	sw, $s0, fib_n
	addi, $sp, $sp, 16
	sw, $v1, fib_x

fib_L5 : 
	lw, $s0, fib_n
	li, $s1, 2
	sub, $t1, $s0, $s1
	sw, $t1, fib_y

fib_L6 : 
	lw, $a0, fib_y
	addi, $sp, $sp, -16
	sw, $ra, 12($sp)
	lw, $s0, fib_x
	sw, $s0, 8($sp)
	lw, $s0, fib_y
	sw, $s0, 4($sp)
	lw, $s0, fib_n
	sw, $s0, 0($sp)
	jal, fib
	lw, $ra, 12($sp)
	lw, $s0, 8($sp)
	sw, $s0, fib_x
	lw, $s0, 4($sp)
	sw, $s0, fib_y
	lw, $s0, 0($sp)
	sw, $s0, fib_n
	addi, $sp, $sp, 16
	sw, $v1, fib_y

fib_L7 : 
	lw, $s0, fib_x
	lw, $s1, fib_y
	add, $t1, $s0, $s1
	sw, $t1, fib_x
	lw, $s0, fib_x
	move, $v1, $s0
	jr, $ra

fib_L8 : 
	jr, $ra
