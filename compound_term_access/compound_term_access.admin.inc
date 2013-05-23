<?php

/** Implementation of hook_perm().
*
*/
function compound_term_access_perm() {

    $permission = array();
    $permission[] = 'administer compound term access';
    return $permission;
}

/** Implementation of hook_menu().
*
*/
function compound_term_access_menu() {

    $menu = array();

    $menu['admin/user/cta'] = array(
        'title' => t('Compound Term Access'),
        'description' => t('Configuration page for Compound Term Access'),
        'access arguments' => array('administer compound term access'),
        'page callback' => 'drupal_get_form',
        'page arguments' => array('compound_term_access_vocab_pick_form'),
        'type' => MENU_NORMAL_ITEM,
    );

    $menu['admin/user/cta/terms'] = array(
        'title' => t('compound term access - term associations'),
        'description' => t('Associate terms with roles'),
        'access arguments' => array('administer compound term access'),
        'page callback' => 'drupal_get_form',
        'page arguments' => array('compound_term_access_term_assoc_form'),
        'type' => MENU_CALLBACK,
    );

    $menu['admin/user/cta/exclude'] = array(
        'title' => t('compound term access - role exclusions'),
        'description' => t('Exclude Roles from Scheme'),
        'access arguments' => array('administer compound term access'),
        'page callback' => 'drupal_get_form',
        'page arguments' => array('compound_term_access_role_exclude_form'),
        'type' => MENU_CALLBACK,
    );

    $menu['admin/user/cta/override'] = array(
        'title' => t('compound term access - role overrides'),
        'description' => t('Assign rights to override roles'),
        'access arguments' => array('administer compound term access'),
        'page callbacks' => 'drupal_get_form',
        'page arguments' => array('compound_term_access_role_override_form'),
        'type' => MENU_CALLBACK,
    );

    return $menu;
}

function compound_term_access_vocab_pick_form($form_state) {

    $vocabularies = array();
    foreach(taxonomy_get_vocabularies() as $vocab) {
        $vocabularies[$vocab->vid] = $vocab->name;
    }

    $current_state = array();
    $result = db_query("SELECT vid FROM {cta_vocabularies}");
    while($row = db_fetch_array($result)) {
        $current_state[] = $row['vid'];
    }

    $form = array();

    $form['header'] = array(
        '#value' => '<h2>Compound Term Access - Configuration Options</h2><p>Choose the vocabularies which will make up your access control requirements. Valid users will require membership in a role that permits access for each vocabulary specified. Terms may be excluded from each vocabulary and roles may be specified as exempt from these requirements. Any user who is a member of a single exempt role will be granted access to all restricted content.</p>',
    );

    $form['vocabularies'] = array(
        '#type' => 'checkboxes',
        '#title' => t('Choose which vocabularies to include in access scheme'),
        '#options' => $vocabularies,
        '#default_value' => $current_state,
    );

    $form['save_cont'] = array(
        '#type' => 'submit',
        '#name' => 'save_cont',
        '#value' => t('Save & Continue'),
    );
    return $form;
}

function compound_term_access_vocab_pick_form_submit($form, $form_state) {

    db_query("DELETE FROM {cta_vocabularies}");
    foreach($form_state['values']['vocabularies'] as $vid) {
        if ($vid != 0) {
            db_query("INSERT {cta_vocabularies} (vid) VALUES (%d)", $vid);
        }
    }
    drupal_goto('admin/user/cta/terms');

}

function compound_term_access_term_assoc_form($form_state) {

    // Build Select Form Objects.
    $result = db_query("SELECT rid, name FROM {role}");
    $roles = array();
    $roles[0] = 'IGNORE TERM';
    while($row = db_fetch_array($result)) {
        $roles[$row['rid']] = $row['name'];
    }
    
    // Get Default values for existing settings.
    $result = db_query("SELECT rid, tid FROM {cta_term_roles}");
    $defaults = array();
    while($row = db_fetch_array($result)) {
        $defaults[$row['tid']] = $row['rid'];
    }

    // Build Form
    $result = db_query("SELECT v.vid, v.name as vocab_name, td.name, td.tid FROM {term_data} td JOIN {cta_vocabularies} cv ON td.vid = cv.vid JOIN {vocabulary} v ON cv.vid = v.vid ORDER BY cv.vid, td.name");
    $current_vid = 0;
 
    $form = array();

    $form['header'] = array(
        '#value' => '<h2>Compound Term Access - Configuration Options</h2><p>Associate each term with a role. Specify IGNORE TERM if you wish a term to have no effect on the current scheme. This is usefull for implementing new terms or depreciating old ones.</p>',
    );

    
    while($row = db_fetch_array($result)) {
        if ($current_vid <> $row['vid']) {
            $form[$row['vid']] = array(
                '#type' => 'fieldset',
                '#title' => t($row['vocab_name']),
                '#collapsible' => TRUE,
                '#collapsed' => FALSE,
            );
            $current_vid = $row['vid'];
        }
        $form[$row['vid']][$row['tid']] = array(
            '#type' => 'select',
            '#title' => t($row['name']),
            '#options' => $roles,
            '#default_value' => $defaults[$row['tid']],
        );
    }

    $form['save_cont'] = array(
        '#type' => 'submit',
        '#name' => 'save_cont',
        '#value' => t('Save & Continue'),
    );
            
    return $form;
}

function compound_term_access_term_assoc_form_submit($form, $form_state) {

    db_query("DELETE FROM {cta_term_roles}");

    foreach($form_state['values'] as $key => $value) {
        if (is_numeric($key)) {
            db_query("INSERT {cta_term_roles} (tid, rid) VALUES (%d, %d)", $key, $value);
        }
    }

    node_access_needs_rebuild(TRUE);
    drupal_goto('admin/user/cta/exclude');
}

function compound_term_access_role_exclude_form($form_state) {

    // Build Select Form Objects.
    $result = db_query("SELECT rid, name FROM {role}");
    $roles = array();
    while($row = db_fetch_array($result)) {
        $roles[$row['rid']] = $row['name'];
    }

    $current_state = array();
    $result = db_query("SELECT rid FROM {cta_exclude_roles}");
    while($row = db_fetch_array($result)) {
        $current_state[] = $row['rid'];
    }

    $form = array();

    $form['header'] = array(
        '#value' => '<h2>Compound Term Access - Configuration Options</h2><p>Choose which roles are excluded from this access scheme. Any user in any of these roles will be granted access to content regardless of membership in roles that are suceptable to this scheme.</p>',
    );

    $form['roles'] = array(
        '#type' => 'checkboxes',
        '#title' => t('Choose which roles to exclude from access scheme'),
        '#options' => $roles,
        '#default_value' => $current_state,
    );

    $form['save_cont'] = array(
        '#type' => 'submit',
        '#name' => 'save_cont',
        '#value' => t('Save & Continue'),
    );

    return $form;
}

function compound_term_access_role_exclude_form_submit($form, $form_state) {

    foreach($form_state['values']['roles'] as $value) {
        if ($value != 0) {
            db_query("INSERT IGNORE INTO {cta_exclude_roles} SET rid = %d, `view` = 0, `update` = 0, `delete` = 0", $value);
        } else {
            db_query("DELETE FROM {cta_exclude_roles} WHERE rid = %d", $value);
        }
    }

    node_access_needs_rebuild(TRUE);
    drupal_goto('admin/user/cta/override');
}

function compound_term_access_role_override_form($form_state) {

    $result = db_query("SELECT er.rid, r.name, er.view, er.`update`, er.`delete` FROM {cta_exclude_roles} er JOIN {role} r ON er.rid = r.rid ORDER BY er.rid"); 
    $no_content = TRUE;

    $form = array();

    $form['header'] = array(
        '#value' => '<h2>Compound Term Access - Configuration Options</h2><p>Grant each override role the permissions you wish them to have. Node Access will grant the rights that have the most control for a piece of content if the user is a member of multiple roles.</p>',
    );

    $form['table_begin'] = array(
        '#value' => '<table><tr class="align_left"><th>Role</th><th>View</th><th>Update</th><th>Delete</th></tr>',
    );

    while($row = db_fetch_array($result)) {

        $no_content = FALSE;

        $form['view_' . $row['rid']] = array(
            '#prefix' => '<tr><td>' . $row['name'] . '</td><td>',
            '#type' => 'checkbox',
            '#suffix' => '</td>',
            '#default_value' => $row['view'],
        );
        $form['update_' . $row['rid']] = array(
            '#prefix' => '<td>',
            '#type' => 'checkbox',
            '#suffix' => '</td>',
            '#default_value' => $row['update'],
        );
        $form['delete_' . $row['rid']] = array(
            '#prefix' => '<td>',
            '#type' => 'checkbox',
            '#suffix' => '</td></tr>',
            '#default_value' => $row['delete'],
        );

    }

    $form['table_end'] = array(
        '#value' => '</table>',
    );

    if ($no_content) {
        $form['message'] = array(
            '#value' => '<h2>There are no roles defined for overrides</h2>',
        );
    }

    $form['save_cont'] = array(
        '#type' => 'submit',
        '#name' => 'save_cont',
        '#value' => t('Save & Continue'),
    );

    return $form;
}

function compound_term_access_role_override_form_submit($form, $form_state) {

    $query_driver = array();

    foreach($form_state['values'] as $key => $value) {
         preg_match('/^(?<mode>[^_]+)_(?<rid>\d+)$/', $key, $matches);
         switch($matches['mode']) {
            case 'view':
            case 'update':
            case 'delete':
                $query_driver[$matches['rid']][$matches['mode']] = $value;
                break;
         }
    }

    foreach($query_driver as $rid => $data) {
        db_query("UPDATE {cta_exclude_roles} SET view = %d, `update` = %d, `delete` = %d WHERE rid = %d", $data['view'], $data['update'], $data['delete'], $rid);
    }

    node_access_needs_rebuild(TRUE);
    drupal_goto('admin/content/node-settings');
}

?>
