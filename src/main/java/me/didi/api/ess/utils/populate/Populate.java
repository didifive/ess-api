package me.didi.api.ess.utils.populate;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class Populate implements CommandLineRunner {
    private final List<PopulateData> populates;

    public Populate(List<PopulateData> populates) {
        this.populates = populates;
    }

    @Override
    public void run(String... args) {
        populates.sort((d1, d2) -> {
            int d1Weight = weight(d1);
            int d2Weight = weight(d2);
            return d1Weight - d2Weight;
        });

        populates.forEach(PopulateData::populate);
    }

    private int weight(PopulateData o) {
        if (o instanceof PopulateStudents)
            return 0;
        if (o instanceof PopulateCourses)
            return 1;
        if (o instanceof PopulateClasses)
            return 2;
        if (o instanceof PopulateSubjects)
            return 3;
        if (o instanceof PopulateRegistrations)
            return 4;
        if (o instanceof PopulateGrades)
            return 5;

        return 100;
    }
}
