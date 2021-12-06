package fr.novlab.bot.commands.manager;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface CommandInfo {

    String name();
    String usage();
    String description();
    String permission();
    Class<? extends SubCommand<?>>[] subCommands() default {};
    boolean autoManagingSubCommands() default true;
    boolean autoCheckPermission() default true;
}
