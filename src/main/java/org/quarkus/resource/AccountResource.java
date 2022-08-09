package org.quarkus.resource;

import org.quarkus.entities.Account;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Path("/accounts")
public class AccountResource{
        Set<Account> accounts = new HashSet<>();

        @GET
        @Produces(MediaType.APPLICATION_JSON)
        public Set<Account> allAccounts(){
            return Collections.emptySet();
        }
}

