package com.liudonglin.transactiond.tr.core.config;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.lang.NonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DependenciesImportSelector implements ImportSelector {

    @Override
    @NonNull
    public String[] selectImports(@NonNull AnnotationMetadata annotationMetadata) {

        List<String> importClasses = new ArrayList<>();
        //importClasses.add("com.codingapi.txlcn.txmsg.MessageConfiguration");

        return importClasses.toArray(new String[0]);
    }
}
