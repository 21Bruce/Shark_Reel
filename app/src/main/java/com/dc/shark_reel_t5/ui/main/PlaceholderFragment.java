package com.dc.shark_reel_t5.ui.main;

import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.ArrayAdapter;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.dc.shark_reel_t5.MainActivity;
import com.dc.shark_reel_t5.R;
import com.google.android.material.tabs.TabLayout;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import android.os.CountDownTimer;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.os.SystemClock;
import android.os.Bundle;
import android.view.View;
import android.widget.Chronometer;
import android.widget.Toast;

import android.os.Bundle;
import android.widget.ToggleButton;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {

    private Chronometer GD;
    private Chronometer B1;
    private Chronometer B2;
    private Chronometer Release;
    private long GDpauseOffset;
    private long B1pauseOffset;
    private long B2pauseOffset;
    private long ReleasepauseOffset;
    private boolean GDrunning;
    private boolean B1running;
    private boolean B2running;
    private boolean Releaserunning;
    private boolean start;
    private TextView timeIn;
    private TextView timeOut;
    private TextView sharkOn;
    private String timeStampIn;
    private String timeStampOut;
    private String timeSharkOn;
    private View root;
    private boolean reset;

    public static final String ARG_SECTION_NUMBER = "section_number";
    private static final String TAG = "placeholderFragment";

    private PageViewModel pageViewModel;

    public static PlaceholderFragment newInstance(int index) {
        PlaceholderFragment fragment = new PlaceholderFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageViewModel = ViewModelProviders.of(this).get(PageViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }
        pageViewModel.setIndex(index);
    }

    public void updatePosition(int position){
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, position);
        this.setArguments(bundle);
    }



    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_main, container, false);

        radioListeners(root);
        editTextListeners(root);
        toggleButtonListener(root);
        Button(root);
        Checked(root);
        Spin(root);

        GD = root.findViewById(R.id.inputLanded);
        B1 = root.findViewById(R.id.inputBlood1);
        B2 = root.findViewById(R.id.inputBlood2);
        Release = root.findViewById(R.id.inputRelease);
        timeIn = root.findViewById(R.id.inputTimeIn);
        timeOut = root.findViewById(R.id.inputTimeOut);
        sharkOn = root.findViewById(R.id.inputSharkOn);

        if(!GDrunning){
            GD.setBase(SystemClock.elapsedRealtime()-GDpauseOffset);
        }
        if(!B1running){
            B1.setBase(SystemClock.elapsedRealtime()-B1pauseOffset);
        }
        if(!B2running){
            B2.setBase(SystemClock.elapsedRealtime()-B2pauseOffset);
        }
        if(!Releaserunning){
            Release.setBase(SystemClock.elapsedRealtime()-ReleasepauseOffset);
        }

        timeIn.setText(timeStampIn);
        timeOut.setText(timeStampOut);
        sharkOn.setText(timeSharkOn);

        final TextView textView = root.findViewById(R.id.section_label);
        pageViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(String.format("Hook #%s", getArguments().getInt(ARG_SECTION_NUMBER)));
            }
        });
        return root;
    }

    private void toggleButtonListener(View root) {
        ToggleButton gpsStartType = root.findViewById(R.id.toggleButtonStart);
        gpsStartType.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Hooks.changeData(getArguments().getInt(ARG_SECTION_NUMBER), 13, "Decimal Minutes");
                }
                else{
                    Hooks.changeData(getArguments().getInt(ARG_SECTION_NUMBER), 13, "Decimal Degrees");
                }
            }
        });

        ToggleButton gpsEndType = root.findViewById(R.id.toggleButtonEnd);
        gpsEndType.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    if(isChecked){
                        Hooks.changeData(getArguments().getInt(ARG_SECTION_NUMBER), 15, "Decimal Minutes");
                    }
                    else{
                        Hooks.changeData(getArguments().getInt(ARG_SECTION_NUMBER), 15, "Decimal Degrees");
                    }
                }
            }
        });
    }

    private void radioListeners(final View root) {
        RadioButton mature = root.findViewById(R.id.radioYes);
        RadioButton immature = root.findViewById(R.id.radioNo);
        RadioButton unknownMaturity = root.findViewById(R.id.radioUnknown);


        mature.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    Hooks.changeData(getArguments().getInt(ARG_SECTION_NUMBER), 5, "Mature");
                }

            }});

        immature.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    Hooks.changeData(getArguments().getInt(ARG_SECTION_NUMBER), 5, "Immature");
                }

            }});

        unknownMaturity.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    Hooks.changeData(getArguments().getInt(ARG_SECTION_NUMBER), 5, "Unknown Maturity");
                }

            }});


        RadioButton jaw = root.findViewById(R.id.radioJaw);
        RadioButton foul = root.findViewById(R.id.radioFoulHooked);

        final EditText hookPlacement = root.findViewById(R.id.inputHookPlacement);

        jaw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Hooks.changeData(getArguments().getInt(ARG_SECTION_NUMBER), 7, "Jaw");
                }
            }
        });
        foul.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Hooks.changeData(getArguments().getInt(ARG_SECTION_NUMBER), 7, hookPlacement.getText().toString());
                    hookPlacement.addTextChangedListener(new TextWatcher(){
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            Hooks.changeData(getArguments().getInt(ARG_SECTION_NUMBER), 7, hookPlacement.getText().toString());
                        }
                    });
                }
            }
        });


        RadioButton male = root.findViewById(R.id.radioMale);
        RadioButton female = root.findViewById(R.id.radioFemale);

        male.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
           @Override
           public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

               if (isChecked) {
                   Hooks.changeData(getArguments().getInt(ARG_SECTION_NUMBER), 8, "Male");
               }

           }});

        female.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    Hooks.changeData(getArguments().getInt(ARG_SECTION_NUMBER), 8, "female");
                }

            }});

        RadioButton zero = root.findViewById(R.id.radioZero);
        RadioButton one = root.findViewById(R.id.radioOne);

        zero.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    Hooks.changeData(getArguments().getInt(ARG_SECTION_NUMBER), 9, "Zero");
                }

            }});

        one.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    Hooks.changeData(getArguments().getInt(ARG_SECTION_NUMBER), 9, "One");
                }

            }});


        RadioButton newCatch = root.findViewById(R.id.radioNew);
        RadioButton recapture = root.findViewById(R.id.radioRecap);
        newCatch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    Hooks.changeData(getArguments().getInt(ARG_SECTION_NUMBER), 10, "New Catch");
                }

            }});

        recapture.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (isChecked) {
                    Hooks.changeData(getArguments().getInt(ARG_SECTION_NUMBER), 10, "Recapture");
                }

            }});

        RadioButton good = root.findViewById(R.id.radioGood);
        RadioButton fair = root.findViewById(R.id.radioFair);
        RadioButton poor = root.findViewById(R.id.radioPoor);
        RadioButton dead = root.findViewById(R.id.radioDead);

        good.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){
                    Hooks.changeData(getArguments().getInt(ARG_SECTION_NUMBER), 20, "Good");
                }

            }
        });
        fair.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){
                    Hooks.changeData(getArguments().getInt(ARG_SECTION_NUMBER), 20, "Fair");
                }

            }
        });
        poor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){
                    Hooks.changeData(getArguments().getInt(ARG_SECTION_NUMBER), 20, "Poor");
                }

            }
        });
        dead.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){
                    Hooks.changeData(getArguments().getInt(ARG_SECTION_NUMBER), 20, "Dead");
                }

            }
        });

        RadioButton deck = root.findViewById(R.id.radioDeck);
        RadioButton platform = root.findViewById(R.id.radioPlatform);
        deck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Hooks.changeData(getArguments().getInt(ARG_SECTION_NUMBER), 28, "Deck");
            }
        });
        platform.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Hooks.changeData(getArguments().getInt(ARG_SECTION_NUMBER), 28, "Platform");
            }
        });


        RadioButton drumline = root.findViewById(R.id.radioDrumline);
        RadioButton gilnet = root.findViewById(R.id.radioGilNet);
        RadioButton hF = root.findViewById(R.id.radioHF);
        RadioButton longline = root.findViewById(R.id.radioLongline);
        final EditText hFSpecified = root.findViewById(R.id.inputHF);
        final EditText longlineSpecified = root.findViewById(R.id.editText23);

        drumline.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){
                    Hooks.changeData(getArguments().getInt(ARG_SECTION_NUMBER), 30, "Drumline");
                }

            }
        });
        gilnet.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){
                    Hooks.changeData(getArguments().getInt(ARG_SECTION_NUMBER), 30, "Gilnet");
                }

            }
        });
        hF.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Hooks.changeData(getArguments().getInt(ARG_SECTION_NUMBER), 30, "HF" + hFSpecified.getText().toString());
                    hFSpecified.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            Hooks.changeData(getArguments().getInt(ARG_SECTION_NUMBER), 30, "HF: " + hFSpecified.getText().toString());
                        }
                    });
                }

            }
        });

        longline.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    Hooks.changeData(getArguments().getInt(ARG_SECTION_NUMBER), 30, "Longline: " + longlineSpecified.getText().toString());
                    longlineSpecified.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            Hooks.changeData(getArguments().getInt(ARG_SECTION_NUMBER), 30, "Longline: " + longlineSpecified.getText().toString());
                        }
                    });
                }

            }
        });

    }
    private void Checked(View root) {

        final CheckBox breakOff = root.findViewById(R.id.checkBreakOff);
        Hooks.changeData(getArguments().getInt(ARG_SECTION_NUMBER), 31, "no");


        breakOff.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                boolean checked = breakOff.isChecked();

                if(checked) {
                    Hooks.changeData(getArguments().getInt(ARG_SECTION_NUMBER), 31, "yes");
                }

                else {
                    Hooks.changeData(getArguments().getInt(ARG_SECTION_NUMBER), 31, "no");
                }

            }
        });

    }

    /**
     * @param root
     * beforeTextChanged and onTextChanged are required even when not used
     */

    private void editTextListeners(View root) {

        final EditText hookSize = root.findViewById(R.id.inputHookSize);
        hookSize.addTextChangedListener(new TextWatcher() {
           @Override
           public void beforeTextChanged(CharSequence s, int start, int count, int after) {

           }

           @Override
           public void onTextChanged(CharSequence s, int start, int before, int count) {

           }

           @Override
           public void afterTextChanged(Editable s) {
                Hooks.changeData(getArguments().getInt(ARG_SECTION_NUMBER), 2, hookSize.getText().toString());
           }
       });


        final EditText tag = root.findViewById(R.id.inputTag);
        tag.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Hooks.changeData(getArguments().getInt(ARG_SECTION_NUMBER), 6, tag.getText().toString());
            }
        });


        final EditText siteName = root.findViewById(R.id.inputSiteName);
        siteName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Hooks.changeData(getArguments().getInt(ARG_SECTION_NUMBER), 11, siteName.getText().toString());
            }
        });

        final EditText gpsStart = root.findViewById(R.id.inputGPSStart);

        gpsStart.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Hooks.changeData(getArguments().getInt(ARG_SECTION_NUMBER), 12, gpsStart.getText().toString());
            }
        });

        final EditText gpsEnd = root.findViewById(R.id.inputGPSEnd);
        gpsEnd.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Hooks.changeData(getArguments().getInt(ARG_SECTION_NUMBER), 14, gpsEnd.getText().toString());
            }
        });

        final EditText depthRange = root.findViewById(R.id.inputDepthRange);
        depthRange.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
               Hooks.changeData(getArguments().getInt(ARG_SECTION_NUMBER), 29, depthRange.getText().toString());
            }
        });

        final EditText pcl = root.findViewById(R.id.inputPCL);
        pcl.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Hooks.changeData(getArguments().getInt(ARG_SECTION_NUMBER), 16, pcl.getText().toString());
            }
        });

        final EditText fl = root.findViewById(R.id.inputFL);
        fl.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Hooks.changeData(getArguments().getInt(ARG_SECTION_NUMBER), 17, fl.getText().toString());
            }
        });

        final EditText tl = root.findViewById(R.id.inputTL);
        tl.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Hooks.changeData(getArguments().getInt(ARG_SECTION_NUMBER), 18, tl.getText().toString());
            }
        });

        final EditText girth = root.findViewById(R.id.inputGirth);
        girth.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Hooks.changeData(getArguments().getInt(ARG_SECTION_NUMBER), 19, girth.getText().toString());
            }
        });


        final EditText noaaCard = root.findViewById(R.id.inputNOAACard);
        noaaCard.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Hooks.changeData(getArguments().getInt(ARG_SECTION_NUMBER), 21, noaaCard.getText().toString());
            }
        });

        final EditText notes = root.findViewById(R.id.inputNotes);
        notes.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Hooks.changeData(getArguments().getInt(ARG_SECTION_NUMBER), 22, notes.getText().toString());
            }
        });

        final EditText timeOut = root.findViewById(R.id.inputTimeOut);
        timeOut.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Hooks.changeData(getArguments().getInt(ARG_SECTION_NUMBER), 23, timeOut.getText().toString());
            }
        });


    }

    private  void Spin (View Root) {
        final Spinner bait = (Spinner) root.findViewById(R.id.baitSpinner);
        final Spinner species = (Spinner) root.findViewById(R.id.speciesSpinner);
        final EditText baitSpecified = root.findViewById(R.id.inputBait);
        final EditText speciesSpecified = root.findViewById(R.id.inputSpecies);

        bait.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if("Other".equals(bait.getSelectedItem())) {

                    Hooks.changeData(getArguments().getInt(ARG_SECTION_NUMBER), 3, "" + baitSpecified.getText().toString());
                    baitSpecified.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            Hooks.changeData(getArguments().getInt(ARG_SECTION_NUMBER), 3, "" + baitSpecified.getText().toString());
                        }
                    });
                }
                else if("-select item-".equals(bait.getSelectedItem())) {
                    Hooks.changeData(getArguments().getInt(ARG_SECTION_NUMBER), 3, "");
                }

                else {
                    Hooks.changeData(getArguments().getInt(ARG_SECTION_NUMBER), 3, "" + bait.getSelectedItem());
                }
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                Hooks.changeData(getArguments().getInt(ARG_SECTION_NUMBER), 3, "");
            }
        });

        species.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if("Other".equals(species.getSelectedItem())) {
                    Hooks.changeData(getArguments().getInt(ARG_SECTION_NUMBER), 4, "" + speciesSpecified.getText().toString());
                    speciesSpecified.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                        }

                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {

                        }

                        @Override
                        public void afterTextChanged(Editable s) {
                            Hooks.changeData(getArguments().getInt(ARG_SECTION_NUMBER), 4, "" + speciesSpecified.getText().toString());
                        }
                    });
                }

                else if("-select item-".equals(species.getSelectedItem())) {
                    Hooks.changeData(getArguments().getInt(ARG_SECTION_NUMBER), 4, "");
                }

                else {
                    Hooks.changeData(getArguments().getInt(ARG_SECTION_NUMBER), 4, "" + species.getSelectedItem());
                }
            }

            public void onNothingSelected(AdapterView<?> adapterView) {
                Hooks.changeData(getArguments().getInt(ARG_SECTION_NUMBER), 4, "");
            }
        });


    }
    private void Button(View root){

        Button hookIn = root.findViewById(R.id.hookIn);
        Button startTimers = root.findViewById(R.id.sharkOn);
        Button pauseGD = root.findViewById(R.id.pauseGD);
        Button pauseB1 = root.findViewById(R.id.pauseB1);
        Button pauseB2 = root.findViewById(R.id.pauseB2);
        Button pauseRelease = root.findViewById(R.id.pauseRelease);
        Button add = root.findViewById(R.id.add);
        GD = root.findViewById(R.id.inputLanded);
        B1 = root.findViewById(R.id.inputBlood1);
        B2 = root.findViewById(R.id.inputBlood2);
        Release = root.findViewById(R.id.inputRelease);
        start = true;
        reset = false;

        final SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        sharkOn = root.findViewById(R.id.inputSharkOn);
        timeIn = root.findViewById(R.id.inputTimeIn);
        timeOut = root.findViewById(R.id.inputTimeOut);

        hookIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                String time = format.format(calendar.getTime());
                timeIn.setText(time);
                timeStampIn = time;
                Hooks.changeData(getArguments().getInt(ARG_SECTION_NUMBER), 0, timeIn.getText().toString());
            }
        });

        startTimers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!GDrunning && !B1running && !B2running && !Releaserunning && start){
                    Calendar calendar = Calendar.getInstance();
                    String time1 = format.format(calendar.getTime());
                    sharkOn.setText(time1);
                    timeSharkOn = time1;
                    Hooks.changeData(getArguments().getInt(ARG_SECTION_NUMBER), 26, sharkOn.getText().toString());
                    GD.setBase(SystemClock.elapsedRealtime() - GDpauseOffset);
                    B1.setBase(SystemClock.elapsedRealtime() - B1pauseOffset);
                    B2.setBase(SystemClock.elapsedRealtime() - B2pauseOffset);
                    Release.setBase(SystemClock.elapsedRealtime() - ReleasepauseOffset);
                    GD.start();
                    B1.start();
                    B2.start();
                    Release.start();
                    GDrunning = true;
                    B1running = true;
                    B2running = true;
                    Releaserunning = true;
                    reset = true;
                    start = false;
                }
            }
        });

        pauseGD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(GDrunning){
                Calendar calendar = Calendar.getInstance();
                String time0 = format.format(calendar.getTime());
                timeOut.setText(time0);
                timeStampOut = time0;
                Hooks.changeData(getArguments().getInt(ARG_SECTION_NUMBER), 1, timeOut.getText().toString());
                GD.stop();
                GDpauseOffset = SystemClock.elapsedRealtime() - GD.getBase();
                GDrunning = false;
                long time =GDpauseOffset;
                time /= 1000;
                String export;
                if(time % 60 < 10){
                   export = (int)(time/60) + ":0" + (int)(time%60);
                }
                else {
                    export = (int) (time / 60) + ":" + (int)(time % 60);
                }
                Hooks.changeData(getArguments().getInt(ARG_SECTION_NUMBER), 24, export);
                }
                else if(!start){
                    GD.start();
                    GD.setBase(SystemClock.elapsedRealtime() - GDpauseOffset);
                    GDrunning = true;
                }
            }
        });

        pauseB1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(B1running){
                    B1.stop();
                    B1pauseOffset = SystemClock.elapsedRealtime() - B1.getBase();
                    B1running = false;
                    long time = B1pauseOffset;
                    time /= 1000;
                    String export;
                    if(time % 60 < 10){
                        export = (int)(time/60) + ":0" + (int)(time%60);
                    }
                    else {
                        export = (int) (time / 60) + ":" + (int)(time % 60);
                    }
                    Hooks.changeData(getArguments().getInt(ARG_SECTION_NUMBER), 25, export);
                }
                else if(!start){
                    B1.start();
                    B1.setBase(SystemClock.elapsedRealtime() - B1pauseOffset);
                    B1running = true;
                }
            }
        });

        pauseB2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(B2running){
                    B2.stop();
                    B2pauseOffset = SystemClock.elapsedRealtime() - B2.getBase();
                    B2running = false;
                    long time = B2pauseOffset;
                    time /= 1000;
                    String export;
                    if(time % 60 < 10){
                        export = (int)(time/60) + ":0" + (int)(time%60);
                    }
                    else {
                        export = (int) (time / 60) + ":" + (int)(time % 60);
                    }
                    Hooks.changeData(getArguments().getInt(ARG_SECTION_NUMBER), 26, export);
                }
                else if(!start){
                    B2.start();
                    B2.setBase(SystemClock.elapsedRealtime() - B2pauseOffset);
                    B2running = true;
                }
            }
        });

        pauseRelease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Releaserunning){
                    Release.stop();
                    ReleasepauseOffset = SystemClock.elapsedRealtime() - Release.getBase();
                    Releaserunning = false;
                    long time = ReleasepauseOffset;
                    time /= 1000;
                    String export;
                    if(time % 60 < 10){
                        export = (int)(time/60) + ":0" + (int)(time%60);
                    }
                    else {
                        export = (int) (time / 60) + ":" + (int)(time % 60);
                    }
                    Hooks.changeData(getArguments().getInt(ARG_SECTION_NUMBER), 27, export);
                }
                else if(!start){
                    Release.start();
                    Release.setBase(SystemClock.elapsedRealtime() - ReleasepauseOffset);
                    Releaserunning = true;
                }

                if(GDrunning){
                    GD.stop();
                    GD.setBase(SystemClock.elapsedRealtime());
                }
                if(B1running){
                    B1.stop();
                    B1.setBase(SystemClock.elapsedRealtime());
                }
                if(B2running){
                    B2.stop();
                    B2.setBase(SystemClock.elapsedRealtime());
                }
            }
        });

    }

}