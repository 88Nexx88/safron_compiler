.data
str1 :	.asciiz "Sqrt 9 = "
a : 	.float 0.0
sqrt_q : 	.float 0.0
sqrt_b : 	.float 0.0
sqrt_c : 	.float 0.0
sqrt_d : 	.float 0.0
sqrt_e : 	.float 0.0
sqrt_f : 	.float 0.0


.text
.globl main

main : 
	li.s, $f31, 9.0
	jal, sqrt
	s.s, $f26, a

L1 : 
	li, $v0, 4
	la, $a0, str1
	syscall

L2 : 
	li, $v0, 2
	l.s, $f12, a
	syscall

L3 : 
	li, $v0, 10
	syscall

sqrt : 
	s.s, $f31, sqrt_q

sqrt_L1 : 
	li.s, $f0, 0.0
	s.s, $f0, sqrt_c

sqrt_L2 : 
	li.s, $f0, 0.0
	s.s, $f0, sqrt_b

sqrt_L3 : 
	l.s, $f0, sqrt_c
	l.s, $f1, sqrt_q
	c.le.s, $f0, $f1
	bc1f, sqrt_L6

sqrt_L4 : 
	li.s, $f1, 1.0
	l.s, $f0, sqrt_b
	add.s, $f2, $f0, $f1
	s.s, $f2, sqrt_b

sqrt_L5 : 
	l.s, $f0, sqrt_b
	l.s, $f1, sqrt_b
	mul.s, $f2, $f0, $f1
	s.s, $f2, sqrt_c
	j, sqrt_L3

sqrt_L6 : 
	li.s, $f1, 1.0
	l.s, $f0, sqrt_b
	sub.s, $f2, $f0, $f1
	s.s, $f2, sqrt_b

sqrt_L7 : 
	l.s, $f0, sqrt_b
	l.s, $f1, sqrt_b
	mul.s, $f2, $f0, $f1
	l.s, $f0, sqrt_q
	sub.s, $f3, $f0, $f2
	s.s, $f3, sqrt_d

sqrt_L8 : 
	li.s, $f1, 2.0
	l.s, $f0, sqrt_q
	mul.s, $f2, $f0, $f1
	s.s, $f2, sqrt_e

sqrt_L9 : 
	l.s, $f0, sqrt_d
	l.s, $f1, sqrt_e
	div.s, $f2, $f0, $f1
	s.s, $f2, sqrt_f

sqrt_L10 : 
	l.s, $f0, sqrt_b
	l.s, $f1, sqrt_f
	add.s, $f2, $f0, $f1
	s.s, $f2, sqrt_q

sqrt_L11 : 
	l.s, $f0, sqrt_f
	l.s, $f1, sqrt_f
	mul.s, $f2, $f0, $f1
	s.s, $f2, sqrt_d

sqrt_L12 : 
	li.s, $f0, 2.0
	l.s, $f1, sqrt_q
	mul.s, $f2, $f0, $f1
	s.s, $f2, sqrt_e

sqrt_L13 : 
	l.s, $f0, sqrt_d
	l.s, $f1, sqrt_e
	div.s, $f2, $f0, $f1
	l.s, $f0, sqrt_q
	sub.s, $f3, $f0, $f2
	s.s, $f3, sqrt_f
	l.s, $f0, sqrt_f
	mov.s, $f26, $f0
	jr, $ra

sqrt_L14 : 
	jr, $ra


