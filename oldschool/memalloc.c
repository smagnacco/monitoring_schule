#include <stdio.h>
#include <stdlib.h>

int main () {
   char *agustin;
   char *isabella;

   /* Initial memory allocation */
   agustin = (char *) malloc(21);
   isabella = (char *) malloc(21);
   
   strcpy(agustin, "agustinvicentemagnacco");
   strcpy(isabella, "isabellachiaramagnacco");
   
   printf("String = %s,  Address = %u\n", agustin, agustin);
   printf("String = %s, Address = %u\n", isabella, isabella);

   free(agustin);
   free(isabella);
   
   return(0);
}
