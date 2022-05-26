VAR INT i, c;

DEF INT fib (INT n);
VAR INT x, y;
{
    IF(n <= 1) THEN{
        RETURN n;
    }
    IF(n > 1) THEN{
        x = n - 1;
        x = fib(x);
        y = n - 2;
        y = fib(y);
        x = x + y;
    }
    RETURN x;
};
{
    c = 0;
    WHILE(c < 10) DO{
        i = fib(c);
        PRINT(i);
        PRINT("\n");
        c = c + 1;
    }
}.