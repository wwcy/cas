/*
 * Copyright 2004 The JA-SIG Collaborative. All rights reserved. See license distributed with this file and available online at
 * http://www.uportal.org/license.html
 */
package org.jasig.cas.ticket;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jasig.cas.authentication.Service;
import org.jasig.cas.authentication.principal.Principal;
import org.jasig.cas.util.UniqueTicketIdGenerator;

/**
 * Domain object representing a Service Ticket. A service ticket grants specific access to a particular service. It will only work for a particular
 * service.
 * 
 * @author Scott Battaglia
 * @version $Id$
 */
public class ServiceTicketImpl extends AbstractTicket implements ServiceTicket {

    protected final Log log = LogFactory.getLog(this.getClass());
    
    private static final long serialVersionUID = 1296808733190507408L;

    private final Service service;

    private final boolean fromNewLogin;

    private final UniqueTicketIdGenerator uniqueTicketIdGenerator;

    private final ExpirationPolicy ticketGrantingTicketExpirationPolicy;

    private final ExpirationPolicy expirationPolicy;

    public ServiceTicketImpl(final String id, final TicketGrantingTicket ticket, final Service service, final boolean fromNewLogin,
        final ExpirationPolicy policy, final UniqueTicketIdGenerator uniqueTicketIdGenerator,
        final ExpirationPolicy ticketGrantingTicketExpirationPolicy) {
        super(id, ticket, policy);

        if (ticket == null || service == null)
            throw new IllegalArgumentException("ticket and service are required parameters");

        this.service = service;
        this.fromNewLogin = fromNewLogin;
        this.uniqueTicketIdGenerator = uniqueTicketIdGenerator;
        this.ticketGrantingTicketExpirationPolicy = ticketGrantingTicketExpirationPolicy;
        this.expirationPolicy = policy;
    }

    /**
     * @see org.jasig.cas.ticket.ServiceTicket#isFromNewLogin()
     */
    public boolean isFromNewLogin() {
        return this.fromNewLogin;
    }

    /**
     * @see org.jasig.cas.ticket.ServiceTicket#getService()
     */
    public Service getService() {
        return this.service;
    }

    /**
     * @see org.jasig.cas.ticket.Ticket#isExpired()
     */
    public boolean isExpired() {
        return super.isExpired() || this.getGrantingTicket().isExpired();
    }

    /**
     * @see org.jasig.cas.ticket.InternalServiceTicket#grantTicketGrantingTicket()
     */
    public TicketGrantingTicket grantTicketGrantingTicket(final Principal principal) {
        return new TicketGrantingTicketImpl(this.uniqueTicketIdGenerator.getNewTicketId(TicketGrantingTicket.PREFIX), this.getGrantingTicket(), principal, this.ticketGrantingTicketExpirationPolicy, this.uniqueTicketIdGenerator, this.expirationPolicy);
    }

}
