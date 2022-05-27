VAR FLOAT a;

DEF FLOAT sqrt(FLOAT q);
VAR FLOAT b, c, d, e, f;
{
    c = 0.0;
    b = 0.0;
    WHILE(c <= q) DO
    {
        b = b + 1.0;
        c = b * b;
    }
    b = b - 1.0;
    d = q - b * b;
    e = q * 2.0;
    f = d / e;
    q = b + f;
    d = f * f;
    e  = 2.0 * q;
    f = q - d / e;
    RETURN f;
};

{
    a = sqrt(9.0);
    PRINT("Sqrt 9 = ");
    PRINT(a);
}.