.data
str1 :	.asciiz "\n"
i : 	.word 0
f_c : 	.word 0
f_x : 	.word 0


.text
.globl main

main : 
	li, $s0, 10
	sw, $s0, i
	lw, $a0, i
	jal, f

L1 : 
	li, $v0, 4
	la, $a0, str1
	syscall
	li, $a0, 127
	jal, f

L2 : 
	li, $v0, 10
	syscall

f : 
	sw, $a0, f_x

f_L1 : 
	lw, $s0, f_x
	li, $s1, 2
	div, $t1, $s0, $s1
	mfhi, $t1
	sw, $t1, f_c

f_L2 : 
	lw, $s0, f_x
	li, $s1, 2
	div, $t1, $s0, $s1
	sw, $t1, f_x

f_L3 : 
	lw, $s0, f_x
	li, $s1, 0
	sgt, $t1, $s0, $s1
	beq, $t1, $zero, f_L4
	lw, $a0, f_x
	addi, $sp, $sp, -12
	sw, $ra, 8($sp)
	lw, $s0, f_c
	sw, $s0, 4($sp)
	lw, $s0, f_x
	sw, $s0, 0($sp)
	jal, f
	lw, $ra, 8($sp)
	lw, $s0, 4($sp)
	sw, $s0, f_c
	lw, $s0, 0($sp)
	sw, $s0, f_x
	addi, $sp, $sp, 12

f_L4 : 
	li, $v0, 1
	lw, $a0, f_c
	syscall

f_L5 : 
	jr, $ra


