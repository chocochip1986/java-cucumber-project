package cdit_automation.data_helpers;

import cdit_automation.repositories.PersonIdRepo;
import cdit_automation.repositories.PersonRepo;
import org.springframework.beans.factory.annotation.Autowired;

public class AbstractService {
    @Autowired
    protected PersonIdRepo personIdrepo;

    @Autowired
    protected PersonRepo personRepo;
}
