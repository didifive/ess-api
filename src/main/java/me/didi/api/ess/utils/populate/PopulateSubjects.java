package me.didi.api.ess.utils.populate;


import me.didi.api.ess.services.SubjectService;
import me.didi.api.ess.entities.Subject;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.logging.Logger;

@Component
public class PopulateSubjects implements PopulateData {

    public static final String ICON_LINK = "iconLink";
    private final Logger logger = Logger.getLogger(getClass().getName());
    private final SubjectService service;

    public PopulateSubjects(SubjectService service) {
        this.service = service;
    }

    public void populate() {
        List<Subject> classes = List.of(
                new Subject(
                        ICON_LINK,
                        "Matemática - FundI",
                        "Matéria de Matemática para Primeiro ano Fundamental"
                ),
                new Subject(
                        ICON_LINK,
                        "Português - FundI",
                        "Matéria de Português para Primeiro ano Fundamental"
                ),
                new Subject(
                        ICON_LINK,
                        "Ciências - FundI",
                        "Matéria de Ciências para Primeiro ano Fundamental"
                ),
                new Subject(
                        ICON_LINK,
                        "História - FundI",
                        "Matéria de História para Primeiro ano Fundamental"
                ),
                new Subject(
                        ICON_LINK,
                        "Geografia - FundI",
                        "Matéria de Geografia para Primeiro ano Fundamental"
                ));

        classes.forEach(service::save);

        logger.info("Subjects saved!");
    }

}
