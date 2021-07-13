# Alfresco Expirable Content

This add-on gives you the ability to add an expiration date to content. When the expiration date is in the past, the content is deleted.

The expiration date is displayed but is not editable in the Share UI. That's because
this property is intended to be set by a workflow, folder rule, script or some other
API. If you let anyone edit that property, someone who ordinarily might not be able
to delete a document could delete something simply by adjusting the expiration date.

The add-on comes with a scheduled job that will run periodically to delete expired
content. By default, the job runs every 5 minutes. To change that, set the following
in alfresco-global.properties:

    ```
    com.metaversant.expirable.jobs.deleteExpiredContent.cronexpression=0 0/5 * * * ?
    com.metaversant.expirable.jobs.deleteExpiredContent.cronstartdelay=240000
    ```

Any time the action runs it will write a report that lists what was deleted. The
report can be found in Data Dictionary/Expirable Content Reports. That folder will
be created automatically when the server starts up for the first time after the
module is installed.

## Manual Installation

Building this project creates two AMPs. One for the repo tier and one for the Share tier.

Copy the AMPs to the server into their respective "amps" and "amps_share" directories,
then use the MMT to install them. For many installations this means switching to
Alfresco Home then running `bin/apply_amps.sh`.

## A note about older releases

Releases older than 2.0.0 were created on behalf of a sponsor called Conexiam.
Starting with 2.0.0 the project changed from the conexiam namespace to the
metaversant namespace. If you have installed a release of this add-on that is
older than 2.0.0, you'll need to remove the cxme:expirable aspect from all
nodes *before* installing version 2.0.0. The reason is that when the namespace
changed the content model changed. The name of the aspect is now mgi:expirable.
