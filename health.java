import java.util.ArrayList;
import java.util.List;

// 🛠️ Command Pattern: Scheduling Appointments
interface Command {
    void execute();
}

class ScheduleAppointmentCommand implements Command {
    private Patient patient;
    private String appointment;

    public ScheduleAppointmentCommand(Patient patient, String appointment) {
        this.patient = patient;
        this.appointment = appointment;
    }

    @Override
    public void execute() {
        System.out.println("📅 Scheduling appointment for: " + patient.getName());
        patient.scheduleAppointment(appointment);
    }
}

// 🚀 State Pattern: Dynamic Medical History Management
interface PatientState {
    void updateMedicalHistory(Patient patient, String newEntry);
}

class HealthyState implements PatientState {
    @Override
    public void updateMedicalHistory(Patient patient, String newEntry) {
        patient.addMedicalHistory("🟢 Healthy: " + newEntry);
        patient.setState(new UnderTreatmentState()); // Transition to treatment state
    }
}

class UnderTreatmentState implements PatientState {
    @Override
    public void updateMedicalHistory(Patient patient, String newEntry) {
        patient.addMedicalHistory("🟡 Under Treatment: " + newEntry);
        patient.setState(new RecoveredState()); // Move to recovered state
    }
}

class RecoveredState implements PatientState {
    @Override
    public void updateMedicalHistory(Patient patient, String newEntry) {
        patient.addMedicalHistory("🟢 Recovered: " + newEntry);
    }
}

// 👨‍⚕️ Patient Class (Now with State Pattern)
class Patient {
    private String name;
    private String medicalHistory = "";
    private List<String> appointments = new ArrayList<>();
    private PatientState state;

    public Patient(String name) {
        this.name = name;
        this.state = new HealthyState(); // Default state
    }

    public String getName() {
        return name;
    }

    public String getMedicalHistory() {
        return medicalHistory;
    }

    public void setState(PatientState state) {
        this.state = state;
    }

    public void addMedicalHistory(String newEntry) {
        this.medicalHistory += newEntry + " | ";
    }

    public void updateMedicalHistory(String newEntry) {
        state.updateMedicalHistory(this, newEntry);
    }

    public void scheduleAppointment(String appointment) {
        appointments.add(appointment);
        System.out.println("✅ Appointment scheduled for " + name + " on " + appointment);
    }

    public void accept(MedicalRecordVisitor visitor) {
        visitor.visit(this);
    }
}

// 🏥 Visitor Pattern: Reviewing Medical Records
interface MedicalRecordVisitor {
    void visit(Patient patient);
}

class MedicalRecordVisitorImpl implements MedicalRecordVisitor {
    @Override
    public void visit(Patient patient) {
        System.out.println("📜 Reviewing medical history for " + patient.getName() + ": " + patient.getMedicalHistory());
    }
}

// 📢 Observer Pattern: Appointment Status Notification
interface Observer {
    void update(Appointment appointment);
}

class DoctorObserver implements Observer {
    @Override
    public void update(Appointment appointment) {
        System.out.println("👨‍⚕️ Doctor notified: " + appointment.getPatient().getName() + "'s appointment is " + appointment.getStatus());
    }
}

class NurseObserver implements Observer {
    @Override
    public void update(Appointment appointment) {
        System.out.println("👩‍⚕️ Nurse notified: " + appointment.getPatient().getName() + "'s appointment is " + appointment.getStatus());
    }
}

class ReceptionistObserver implements Observer {
    @Override
    public void update(Appointment appointment) {
        System.out.println("🛎️ Receptionist notified: " + appointment.getPatient().getName() + "'s appointment is " + appointment.getStatus());
    }
}

class Appointment {
    private Patient patient;
    private String date;
    private String status;
    private List<Observer> observers = new ArrayList<>();

    public Appointment(Patient patient, String date) {
        this.patient = patient;
        this.date = date;
        this.status = "Scheduled";
    }

    public void addObserver(Observer observer) {
        observers.add(observer);
    }

    public void notifyObservers() {
        for (Observer observer : observers) {
            observer.update(this);
        }
    }

    public void changeStatus(String status) {
        this.status = status;
        System.out.println("🔄 Appointment status changed to: " + status);
        notifyObservers();
    }

    public Patient getPatient() {
        return patient;
    }

    public String getStatus() {
        return status;
    }
}

// 💊 Strategy Pattern: Different Treatment Methods
interface TreatmentStrategy {
    void apply(Patient patient);
}

class ChemotherapyStrategy implements TreatmentStrategy {
    @Override
    public void apply(Patient patient) {
        System.out.println("💉 Applying chemotherapy treatment for " + patient.getName());
    }
}

class SurgeryStrategy implements TreatmentStrategy {
    @Override
    public void apply(Patient patient) {
        System.out.println("🛠️ Scheduling surgery for " + patient.getName());
    }
}

class RadiationTherapyStrategy implements TreatmentStrategy {
    @Override
    public void apply(Patient patient) {
        System.out.println("☢️ Applying radiation therapy for " + patient.getName());
    }
}

class TreatmentContext {
    private TreatmentStrategy strategy;

    public TreatmentContext(TreatmentStrategy strategy) {
        this.strategy = strategy;
    }

    public void applyTreatment(Patient patient) {
        strategy.apply(patient);
    }
}

// 🔗 Chain of Responsibility: Medical Checkups
abstract class MedicalHandler {
    protected MedicalHandler nextHandler;

    public MedicalHandler(MedicalHandler nextHandler) {
        this.nextHandler = nextHandler;
    }

    public abstract void handle(Patient patient);
}

class InitialCheckupHandler extends MedicalHandler {
    public InitialCheckupHandler(MedicalHandler nextHandler) {
        super(nextHandler);
    }

    @Override
    public void handle(Patient patient) {
        System.out.println("🔍 Performing initial checkup for " + patient.getName());
        if (nextHandler != null) nextHandler.handle(patient);
    }
}

class DiagnosisHandler extends MedicalHandler {
    public DiagnosisHandler(MedicalHandler nextHandler) {
        super(nextHandler);
    }

    @Override
    public void handle(Patient patient) {
        System.out.println("🔬 Diagnosing " + patient.getName());
        if (nextHandler != null) nextHandler.handle(patient);
    }
}

class PrescriptionHandler extends MedicalHandler {
    public PrescriptionHandler(MedicalHandler nextHandler) {
        super(nextHandler);
    }

    @Override
    public void handle(Patient patient) {
        System.out.println("💊 Prescribing medication for " + patient.getName());
        if (nextHandler != null) nextHandler.handle(patient);
    }
}

// 🚀 Main Application
public class health {
    public static void main(String[] args) {
        Patient patient = new Patient("John Doe");

        // 🛠️ Command: Schedule an appointment
        Command scheduleCommand = new ScheduleAppointmentCommand(patient, "2025-02-07 10:00");
        scheduleCommand.execute();

        // 📜 Visitor: Review medical history
        MedicalRecordVisitor visitor = new MedicalRecordVisitorImpl();
        patient.accept(visitor);

        // 📢 Observer: Notify multiple observers
        Appointment appointment = new Appointment(patient, "2025-02-07 10:00");
        appointment.addObserver(new DoctorObserver());
        appointment.addObserver(new NurseObserver());
        appointment.addObserver(new ReceptionistObserver());
        appointment.changeStatus("Completed");

        // 💊 Strategy: Apply radiation therapy
        TreatmentContext treatmentContext = new TreatmentContext(new RadiationTherapyStrategy());
        treatmentContext.applyTreatment(patient);

        // 🔗 Chain of Responsibility: Perform medical checks
        MedicalHandler checkupHandler = new InitialCheckupHandler(new DiagnosisHandler(new PrescriptionHandler(null)));
        checkupHandler.handle(patient);
    }
}
