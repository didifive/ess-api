package me.didi.api.ess.utils.populate;


import me.didi.api.ess.entities.Course;
import me.didi.api.ess.entities.Message;
import me.didi.api.ess.entities.News;
import me.didi.api.ess.entities.Shortcut;
import me.didi.api.ess.enums.Frequency;
import me.didi.api.ess.services.CourseService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

@Component
public class PopulateCourses implements PopulateData {
    public static final String ICON_URL = "iconURL";
    private final Logger logger = Logger.getLogger(getClass().getName());
    private final CourseService service;

    public PopulateCourses(CourseService service) {
        this.service = service;
    }

    public void populate() {
        Set<Message> messages = Set.of(
                new Message(
                        ICON_URL,
                        "Mensagem 1",
                        "!Descrição da Mensagem Um!"
                ),
                new Message(
                        ICON_URL,
                        "Mensagem 2",
                        "!!Descrição da Mensagem Dois!!"
                ),
                new Message(
                        ICON_URL,
                        "Mensagem 3",
                        "!!!Descrição da Mensagem Três!!!"
                )
        );

        Set<News> news = Set.of(
                new News(
                        ICON_URL,
                        "News Um",
                        "!Descrição da Mensagem Um!"
                ),
                new News(
                        ICON_URL,
                        "News Dois",
                        "!!Descrição da News Dois!!"
                ),
                new News(
                        ICON_URL,
                        "Mensagem 3",
                        "!!!Descrição da News Três!!!"
                )
        );

        Set<Shortcut> shortcuts = Set.of(
                new Shortcut(
                        ICON_URL,
                        "Atalho 1",
                        "!Descrição do Atalho 1!",
                        "linkShortcut1"
                ),
                new Shortcut(
                        ICON_URL,
                        "Atalho 2",
                        "!!Descrição do Atalho 2!!",
                        "linkShortcut2"
                ),
                new Shortcut(
                        ICON_URL,
                        "Atalho 3",
                        "!!!Descrição do Atalho 3!!!",
                        "linkShortcut3"
                )
        );


        List<Course> courses = List.of(
                new Course("Curso 1",
                        Frequency.YEARLY),
                new Course("Curso 2",
                        Frequency.SEMIANNUALLY),
                new Course("Curso 3",
                        Frequency.MONTHLY));

        courses.forEach(c -> {
            service.save(c);
            messages.forEach(m -> service.addMessage(c.getId(), m));
            news.forEach(n -> service.addNews(c.getId(), n));
            shortcuts.forEach(s -> service.addShortcut(c.getId(), s));
        });

        logger.info("Courses saved!");
    }

}
