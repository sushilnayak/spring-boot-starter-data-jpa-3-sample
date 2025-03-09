package com.nayak.springdatajpasample.service;

import com.nayak.springdatajpasample.entity.Employee;
import com.nayak.springdatajpasample.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.history.Revision;
import org.springframework.data.history.Revisions;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class EmployeeRevisionService {

    private final EmployeeRepository employeeRepository;

    public Revisions<Integer, Employee> findRevisions(Long employeeId) {
        return employeeRepository.findRevisions(employeeId);
    }

    public Optional<Revision<Integer, Employee>> findLastChangeRevision(Long employeeId) {
        return employeeRepository.findLastChangeRevision(employeeId);
    }

    public Optional<Revision<Integer, Employee>> findRevision(Long employeeId, Integer revisionNumber) {
        return employeeRepository.findRevision(employeeId, revisionNumber);
    }

    public Revisions<Integer, Employee> findRevisionsOrderByRevisionDesc(Long employeeId) {
        return employeeRepository.findRevisions(employeeId).reverse(); // , RevisionSort.desc()
    }

    public Optional<Employee> findEmployeeAtRevision(Long employeeId, Integer revisionNumber) {
        return employeeRepository.findRevision(employeeId, revisionNumber)
                .map(Revision::getEntity);
    }

    public Optional<Employee> findEmployeeAtLastRevision(Long employeeId) {
        return employeeRepository.findLastChangeRevision(employeeId)
                .map(Revision::getEntity);
    }
}