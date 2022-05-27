VAR INT i;

DEF VOID f(INT x);
VAR INT c;
{
   c = x % 2;
   x = x / 2;
   IF(x > 0) THEN
   {
      f(x);
   }
   PRINT(c);

};

{
    i = 10;
    f(i);
    PRINT("\n");
    f(127);
}.