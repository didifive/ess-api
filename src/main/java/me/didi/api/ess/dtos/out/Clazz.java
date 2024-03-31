package me.didi.api.ess.dtos.out;

public record Clazz(String id,
                    String name,
                    Course course,
                    String initDate,
                    String recoveryDate,
                    String endDate
) {
}
