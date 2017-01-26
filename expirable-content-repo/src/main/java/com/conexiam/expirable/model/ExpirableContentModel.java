package com.conexiam.expirable.model;

import org.alfresco.service.namespace.QName;

public class ExpirableContentModel {
    public static final String CONEXIAM_NAMESPACE = "http://www.conexiam.com/model/expirable/1.0";
    public static final QName ASPECT_EXPIRABLE = QName.createQName(CONEXIAM_NAMESPACE, "expirable");
    public static final QName PROP_EXPIRATION_DATE = QName.createQName(CONEXIAM_NAMESPACE, "expirationDate");
}
