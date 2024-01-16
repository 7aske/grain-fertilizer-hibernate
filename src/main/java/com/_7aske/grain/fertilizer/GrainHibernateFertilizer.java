package com._7aske.grain.fertilizer;

import com._7aske.grain.core.component.Grain;
import com._7aske.grain.core.configuration.GrainFertilizer;
import com._7aske.grain.core.context.ApplicationContext;
import com._7aske.grain.core.reflect.classloader.GrainClassLoader;
import com._7aske.grain.core.reflect.classloader.GrainJarClassLoader;
import jakarta.persistence.Entity;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

/**
 * Integration with Hibernate. All of Hibernates required configuration properties
 * are read from System#getProperties() which are loaded from application.properties
 * file. Only thing that is required to do "manually" is to scan for and load
 * Entity classes which this class is responsible for. This replaces Grain's
 * persistence layer, therefore it must not be enabled in the application.properties.
 */
@GrainFertilizer
public class GrainHibernateFertilizer {
    @Grain
    public SessionFactory sessionFactory(ApplicationContext context) {
        Configuration configuration = new Configuration();

        GrainClassLoader grainClassLoader = new GrainJarClassLoader(context.getPackage());
        grainClassLoader.loadClasses(cl -> cl.isAnnotationPresent(Entity.class))
                .forEach(configuration::addAnnotatedClass);

        return configuration.buildSessionFactory();
    }
}
