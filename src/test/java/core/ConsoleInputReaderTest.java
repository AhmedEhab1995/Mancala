package core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import static org.mockito.Mockito.doThrow;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

public class ConsoleInputReaderTest {
    @Mock
    private BufferedReader mockBufferedReader;
    @Mock
    private ConsoleInputReader mockInputReader;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();

    @BeforeEach
    public void setUp() throws IOException {
        MockitoAnnotations.openMocks(this);
        doThrow(new IOException()).when(mockBufferedReader).close();
        doThrow(new IOException()).when(mockBufferedReader).readLine();
        mockInputReader.reader = mockBufferedReader;
        System.setOut(new PrintStream(outContent));
    }
    @Test
    public void shouldHandleExceptionsDuringReading(){
        assertDoesNotThrow(() ->
                mockInputReader.readLine(ConsoleInputReader.InputType.FIRST_PLAYER_NAME));
    }

    @Test
    public void shouldHandleExceptionsDuringClosing(){
        assertDoesNotThrow(() ->
                mockInputReader.close());
    }
}
