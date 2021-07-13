package com.metaversant.expirable.model;

import org.alfresco.service.namespace.QName;

public class ExpirableContentModel {
    public static final String METAVERSANT_NAMESPACE = "http://www.metaversant.com/model/expirable/1.0";
    public static final QName ASPECT_EXPIRABLE = QName.createQName(METAVERSANT_NAMESPACE, "expirable");
    public static final QName PROP_EXPIRATION_DATE = QName.createQName(METAVERSANT_NAMESPACE, "expirationDate");
}
