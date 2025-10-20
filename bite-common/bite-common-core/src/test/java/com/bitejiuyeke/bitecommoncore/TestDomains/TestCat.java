package com.bitejiuyeke.bitecommoncore.TestDomains;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString
public class TestCat extends TestAnimal{
    private String sound;
    private Integer age;
}
