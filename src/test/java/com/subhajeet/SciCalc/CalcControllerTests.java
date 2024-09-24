package com.subhajeet.SciCalc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
public class CalcControllerTests {
    @Autowired
    private MockMvc mockMvc;

    @ParameterizedTest
    @CsvSource({
            "48.56, 6.96",
            "9.0, 3.00",
            "0.0, 0.00"
    })
    public void testPositiveRoot(float input, String expectedOutput) throws Exception {
        mockMvc.perform(get("/root")
                        .param("x", String.valueOf(input)))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedOutput));
    }

    @ParameterizedTest
    @CsvSource({
            "-5.5",
            "-3",
    })
    public void testNegativeRoot(float input) throws Exception{
        mockMvc.perform(get("/root")
                        .param("x", String.valueOf(input)))
                .andExpect(status().isOk())
                .andExpect(content().string("Negative input to square root"));
    }

    @ParameterizedTest
    @CsvSource({
            "4, 24",
            "10, 3628800",
            "0, 1"
    })
    public void testValidFactorials(int input, String expectedOutput) throws Exception{
        mockMvc.perform(get("/factorial")
                        .param("x", String.valueOf(input)))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedOutput));
    }

    @ParameterizedTest
    @CsvSource({
            "-4, Factorial not defined for negative integers",
            "11, Input too large for factorial"
    })
    public void testInvalidFactorials(int input, String expectedOutput) throws Exception{
        mockMvc.perform(get("/factorial")
                        .param("x", String.valueOf(input)))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedOutput));
    }

    @ParameterizedTest
    @CsvSource({
            "48.56, 3.88",
            "2.71, 0.99",
    })
    public void testPositiveLog(float input, String expectedOutput) throws Exception {
        mockMvc.perform(get("/ln")
                        .param("x", String.valueOf(input)))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedOutput));
    }

    @ParameterizedTest
    @CsvSource({
            "-5.5",
            "0",
    })
    public void testNegativeLog(float input) throws Exception{
        mockMvc.perform(get("/root")
                        .param("x", String.valueOf(input)))
                .andExpect(status().isOk())
                .andExpect(content().string("Negative/Zero input to natural logarithm"));
    }

    @ParameterizedTest
    @CsvSource({
            "3, 2, 9.00",
            "4.56, 0, 1.00",
    })
    public void testPower(float x, float b, String expectedOutput) throws Exception {
        mockMvc.perform(get("/power")
                        .param("x", String.valueOf(x))
                        .param("b", String.valueOf(b)))
                .andExpect(status().isOk())
                .andExpect(content().string(expectedOutput));
    }
}
