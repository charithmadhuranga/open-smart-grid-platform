package org.opensmartgridplatform.adapter.ws.core.application.services;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.opensmartgridplatform.adapter.ws.core.application.criteria.SearchEventsCriteria;
import org.opensmartgridplatform.domain.core.entities.Device;
import org.opensmartgridplatform.domain.core.entities.Event;
import org.opensmartgridplatform.domain.core.entities.Organisation;
import org.opensmartgridplatform.domain.core.repositories.EventRepository;
import org.opensmartgridplatform.domain.core.specifications.EventSpecifications;
import org.opensmartgridplatform.shared.application.config.PagingSettings;
import org.opensmartgridplatform.shared.exceptionhandling.FunctionalException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
public class DeviceManagementServiceTest {
    @Mock
    private Page<String> page;
    @Mock
    private EventRepository eventRepository;
    @Mock
    private DomainHelperService domainHelperService;
    @Mock
    private SearchEventsCriteria criteria;
    @Mock
    private PagingSettings pagingSettings;
    @Mock
    private EventSpecifications eventSpecifications;
    @InjectMocks
    private DeviceManagementService deviceManagementService;

    @Test
    public void findEventsWithNoDescriptionStartsWithTest() throws FunctionalException {
        when(this.criteria.getOrganisationIdentification()).thenReturn("orgIdentification");
        when(this.criteria.getDeviceIdentification()).thenReturn("deviceIdentification");
        when(this.domainHelperService.findOrganisation(any())).thenReturn(new Organisation());
        doNothing().when(this.pagingSettings).updatePagingSettings(any());
        when(this.domainHelperService.findDevice(any())).thenReturn(new Device());
        doNothing().when(this.domainHelperService).isAllowed(any(), any(), any());
        when(this.eventSpecifications.isFromDevice(any())).thenReturn(new Specification<Event>() {
            private static final long serialVersionUID = 2946693984484298490L;

            @Override
            public Predicate toPredicate(final Root<Event> root, final CriteriaQuery<?> criteriaQuery,
                    final CriteriaBuilder criteriaBuilder) {
                return null;
            }
        });
        when(this.pagingSettings.getPageNumber()).thenReturn(1);
        when(this.pagingSettings.getPageSize()).thenReturn(1);
        when(this.eventRepository.findAll((Specification<Event>) any(), any(Pageable.class))).thenReturn(null);

        final Page<Event> resultPage = this.deviceManagementService.findEvents(this.criteria);

        Assertions.assertNull(resultPage);
    }
}
