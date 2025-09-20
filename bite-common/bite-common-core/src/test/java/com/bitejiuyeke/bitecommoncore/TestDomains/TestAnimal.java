package com.bitejiuyeke.bitecommoncore.TestDomains;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type")
@JsonSubTypes({
    @JsonSubTypes.Type(value = TestDog.class, name = "dog"),
    @JsonSubTypes.Type(value = TestCat.class, name = "cat")
})
@Getter
@Setter
@ToString
public class TestAnimal {
    private String name;
}
