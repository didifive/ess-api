package me.didi.api.ess.dtos.out;

public record Message(String id,
                      String icon,
                      String title,
                      String description,
                      String dateTime
) {
}
