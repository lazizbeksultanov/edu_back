package uz.gvs.admin_crm.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.gvs.admin_crm.entity.User;
import uz.gvs.admin_crm.entity.enums.Gender;
import uz.gvs.admin_crm.entity.enums.RoleName;
import uz.gvs.admin_crm.payload.UserDto;
import uz.gvs.admin_crm.repository.AttachmentRepository;
import uz.gvs.admin_crm.repository.RegionRepository;
import uz.gvs.admin_crm.repository.RoleRepository;
import uz.gvs.admin_crm.repository.UserRepository;

import java.text.SimpleDateFormat;
import java.util.HashSet;


@Service
public class UserService {
    @Autowired
    UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    AttachmentRepository attachmentRepository;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    RegionRepository regionRepository;
    @Autowired
    ApiResponseService apiResponseService;

    public User makeUser(UserDto userDto, RoleName roleName) {
        try {
            if (userRepository.existsByPhoneNumber(userDto.getPhoneNumber())) {
                return null;
            }
            User user = new User();
            SimpleDateFormat formatter1 = new SimpleDateFormat("dd/MM/yyyy");
            user.setFullName(userDto.getFullName());
            user.setPhoneNumber(userDto.getPhoneNumber());
            user.setDescription(userDto.getDescription());
            user.setRegion(userDto.getRegionId() != null ? regionRepository.findById(userDto.getRegionId()).orElseThrow(() -> new ResourceNotFoundException("get region")) : null);
            user.setGender(Gender.valueOf(userDto.getGender()));
            user.setBirthDate(userDto.getBirthDate() != null ? formatter1.parse(userDto.getBirthDate()) : null);
            user.setRoles(new HashSet<>(roleRepository.findAllByRoleName(roleName)));
            return userRepository.save(user);
        } catch (Exception e) {
            return null;
        }

    }


    public boolean checkPhoneNumber(String phoneNumber) {
        return !userRepository.existsByPhoneNumber(phoneNumber);
    }

    public User editUser(UserDto userDto, User user,RoleName roleName) {
        try {
            SimpleDateFormat formatter1 = new SimpleDateFormat("dd/MM/yyyy");
            user.setFullName(userDto.getFullName());
            user.setPhoneNumber(userDto.getPhoneNumber());
            user.setDescription(userDto.getDescription());
            user.setRegion(userDto.getRegionId() != null ? regionRepository.findById(userDto.getRegionId()).orElseThrow(() -> new ResourceNotFoundException("get region")) : null);
            user.setGender(Gender.valueOf(userDto.getGender()));
            user.setBirthDate(userDto.getBirthDate() != null ? formatter1.parse(userDto.getBirthDate()) : null);
            user.setRoles(new HashSet<>(roleRepository.findAllByRoleName(roleName)));
            return userRepository.save(user);
        }catch (Exception e){
            return null;
        }
    }
}
