.data
r : 	.float 0.0


.text
.globl main

main : 
	li.s, $f0, 0.0
	s.s, $f0, r

L1 : 
	li.s, $f1, 30.0
	l.s, $f0, r
	c.le.s, $f0, $f1
	bc1f, L5

L2 : 
	li.s, $f1, 15.1
	l.s, $f0, r
	c.lt.s, $f0, $f1
	bc1t, L4

L3 : 
	jal, L5

L4 : 
	li.s, $f1, 1.5
	l.s, $f0, r
	add.s, $f2, $f0, $f1
	s.s, $f2, r
	j, L1

L5 : 
	li, $v0, 2
	l.s, $f12, r
	syscall

L6 : 
	li, $v0, 10
	syscall


