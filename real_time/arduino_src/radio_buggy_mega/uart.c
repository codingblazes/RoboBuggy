#include "uart.h"

/* http://www.cs.mun.ca/~rod/Winter2007/4723/notes/serial/serial.html */

void uart_init(void) {
    UBRR0H = UBRRH_VALUE;
    UBRR0L = UBRRL_VALUE;
    
#if USE_2X
    UCSR0A |= _BV(U2X0);
#else
    UCSR0A &= ~(_BV(U2X0));
#endif

    UCSR0C = _BV(UCSZ01) | _BV(UCSZ00); /* 8-bit data */
    UCSR0B = _BV(RXEN0) | _BV(TXEN0);   /* Enable RX and TX */

    // set up file stream using this uart for stdout/stdin
    uart_stdio.put = uart_putchar;
    uart_stdio.get = uart_getchar;
    uart_stdio.flags = _FDEV_SETUP_RW;

    stdout = stdin = &uart_stdio;
}

int uart_putchar(char c, FILE *stream) {
    // if (c == '\n') {
    //     uart_putchar('\r', stream);
    // }
    loop_until_bit_is_set(UCSR0A, UDRE0);
    UDR0 = c;
    return 0;
}

int uart_getchar(FILE *stream) {
    // loop_until_bit_is_set(UCSR0A, RXC0);
    if(UCSR0A & _BV(RXC0)) {
        // send back new data if available
        return (int)UDR0;
    } else {
        return EOF;
    }
}